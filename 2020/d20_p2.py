import utils
import math
import numpy as np

class Puzzle:
    def __init__(self,data,key):
        self.data = data
        self.key = key
        self.borders = self.extract_borders()
        return

    def stringify_data(self):
        output = ''
        for line in self.data:
            output += ''.join(line)
            output += '\n'
        return output

    def __repr__(self):
        output = str(self.key) + '\n'
        output += self.stringify_data()
        return output

    def extract_top_bottom(self):
        top = ''.join([el for el in self.data[0]])
        bottom = ''.join([el for el in self.data[-1]])
        return top, bottom
    
    def extract_right_left(self):
        right = ''
        left = ''
        for __i in range(len(self.data)):
            left += self.data[__i][0]
            right += self.data[__i][-1]
    
        return right, left
    
    def extract_borders(self):
        top, bottom = self.extract_top_bottom()
        right,left = self.extract_right_left()

        return [top, right, bottom, left]

    def rotate_right(self):
        rows = len(self.data)
        cols = len(self.data[0])
        new_data = [['' for col in range(cols)] for row in range(rows)]

        for i in range(rows):
            for j in range(cols):
                new_data[j][cols - i - 1] = self.data[i][j]

        return new_data

    def rotate(self):
        self.data = self.rotate_right()
        self.borders = self.extract_borders()
        return

    def flip_v(self):
        new_data = []
        for row in range(len(self.data)):
            new_data.append(self.data[row][::-1])

        return new_data

    def flip_h(self):
        rows = len(self.data)
        new_data = [self.data[rows - row - 1] for row in range(rows)]

        return new_data

    def flip(self, vertical):
        if vertical:
            self.data = self.flip_v()
        else:
            self.data = self.flip_h()

        self.borders = self.extract_borders()
        return

    def remove_borders(self):
        new_data = [[self.data[row][col] for col in range(1,len(self.data)-1)] for row in range(1,len(self.data[0])-1)]
        self.data = new_data
        return


class Board:
    def __init__(self, puzzles):
        size = 2 * int(math.sqrt(len(puzzles))) + 1
        board = [[-1 for __i in range(size)] for __j in range(size)]
        self.board = np.array(board)
        self.puzzles = puzzles
        chosen = puzzles[0]
        self.board[size//2, size//2] = chosen.key
        self.completed = []
        self.to_be_done = [chosen]
        self.full = None
        self.joint = None
        return

    def get_by_key(self, key):
        for puzzle in self.puzzles:
            if puzzle.key == key:
                return puzzle
        return None

    def solve_board(self):
        while len(self.completed) < len(self.puzzles):
            current = self.to_be_done.pop(0)
            self.process_one(current)

        self.remove_empty_fields()
        self.full = self.create_full_board()
        self.run_sanity_check()
        self.joint = self.join_board()
        return

    def process_one(self, center):
        idx = np.where(self.board == center.key)
    
        for puzzle in self.puzzles:
            if center.key == puzzle.key:
               continue 

            if self.completed.count(puzzle.key) > 0:
                continue

            if self.to_be_done.count(puzzle) > 0:
                continue

            matched = self.match_to_one(center, puzzle)
            if matched != None:
                #print('{} matches {}'.format(center.key, puzzle.key))
                new_index = self.get_new_index(idx, matched)
                if self.fits_all(new_index, puzzle):
                    self.board[self.get_new_index(idx, matched)] = puzzle.key
                    self.to_be_done.append(puzzle)
                else:
                    print('{} did not match all'.format(puzzle.key))
                    print(puzzle)
                    print(center)

        self.completed.append(center.key)
        return
    
    def match_to_one(self, p1, p2):
        #print('====================\n checking if {} matches with {}'.format(p1.key, p2.key))
    
        for border in range(4):
            #print('  - checking matches to border {} of puzzle {}'.format(border, p1.key))
            p1_border = p1.borders[border]
            p2_border_idx = (border + 2) % 4
            for __j in range(4):
                if p1_border == p2.borders[p2_border_idx]:
                    return border

                if p1_border == p2.borders[p2_border_idx][::-1]:
                    p2.flip(border % 2 == 0)
                    return border

                p2.rotate()

        #print('no maches found')
    
        return None

    def fits_all(self, pos, puzzle):
        top = (pos[0] - 1, pos[1])
        bottom = (pos[0] + 1, pos[1])
        left = (pos[0], pos[1] - 1)
        right = (pos[0], pos[1] + 1)
        p_top = self.board[top]
        p_bottom = self.board[bottom]
        p_left = self.board[left]
        p_right = self.board[right]
        #print(puzzle.key, p_top, p_bottom, p_left, p_right)

        if p_top != -1:
            check = self.get_by_key(p_top)
            if puzzle.borders[2] != check.borders[0]:
                return False
        
        if p_bottom != -1:
            check = self.get_by_key(p_bottom)
            if puzzle.borders[0] != check.borders[2]:
                return False
        
        if p_left != -1:
            check = self.get_by_key(p_left)
            if puzzle.borders[3] != check.borders[1]:
                return False

        if p_right != -1:
            check = self.get_by_key(p_right)
            if puzzle.borders[1] != check.borders[3]:
                return False

        return True


    def get_new_index(self, old, border):
        if border == 0:
            return (old[0] + 1, old[1])
        elif border == 2:
            return (old[0] - 1, old[1])
        elif border == 1:
            return (old[0], old[1] + 1)
        elif border == 3:
            return (old[0], old[1] - 1)
        else:
            print('Whaaaaat?', border)

    def remove_empty_fields(self):
        new_b = []
        for row in self.board:
            if not self.is_row_empty(row):
                new_b.append(row)

        self.board = np.array(new_b)
        cols_to_del = []
        for col in range(len(self.board[0])):
            if self.is_col_empty(col):
                cols_to_del.append(col)

        self.board = np.delete(self.board, cols_to_del, 1)

    def is_row_empty(self, row):
        for el in row:
            if el != -1:
                return False

        return True

    def is_col_empty(self, col):
        for row in range(len(self.board)):
            if self.board[row, col] != -1:
                return False

        return True

    def run_sanity_check(self):
        #print('running sanity checks')
        rows = len(self.board)
        for __i in range(rows - 1):
            if not self.full[__i,0].data[-1] == self.full[__i+1,0].data[0]:
                #print(' -> bottom of row {} does not match top of row {}'.format(__i, __i+1))
                if not self.full[__i,0].data[0] == self.full[__i+1,0].data[0]:
                    #print(' -> top of row {} does not match top of row {}'.format(__i, __i+1))
                    if not self.full[__i,0].data[-1] == self.full[__i+1,0].data[-1]:
                        #print(' -> bottom of row {} does not match bottom of row {}'.format(__i, __i+1))
                        #print('  -> both need a flip')
                        for puzzle in self.full[__i]:
                            puzzle.flip(False)
                        for puzzle in self.full[__i+1]:
                            puzzle.flip(False)

                    else:
                        #print('  -> only bottom needs a flip')
                        for puzzle in self.full[__i+1]:
                            puzzle.flip(False)
                else:
                    #print('  -> only top needs a flip')
                    for puzzle in self.full[__i]:
                        puzzle.flip(False)

        return

    def create_full_board(self):
        rows = len(self.board)
        cols = len(self.board[0])
        copy = np.array([[None for c in range(cols)] for r in range(rows)])
        for i in range(rows):
            for j in range(cols):
                copy[i,j] = self.get_by_key(self.board[i,j])

        return copy

    def join_board(self):
        for row in self.full:
            for el in row:
                el.remove_borders()

        count = len(self.full[0,0].data)

        joint = []
        for line in self.full:
            for __j in range(count):
                tmp = []
                for el in line:
                    tmp.extend(el.data[__j])

                joint.append(tmp)

        return joint

    def show_board_as_string(self):
        output = ''
        for line in self.joint:
            output += ''.join(line)
            output += '\n'
        return output

class MonsterHunter:
    def __init__(self, image):
        self.image = image
        return
        
    def rotate(self):
        rows = len(self.image)
        cols = len(self.image[0])
        new_image = [['' for col in range(cols)] for row in range(rows)]

        for i in range(rows):
            for j in range(cols):
                new_image[j][cols - i - 1] = self.image[i][j]

        self.image = new_image
        return

    def flip(self):
        new_image = []
        for row in range(len(self.image)):
            new_image.append(self.image[row][::-1])

        self.image = new_image
        return

    def __repr__(self):
        output = ''
        for line in self.image:
            output += ''.join(line)
            output += '\n'
        return output

def extract_puzzles(lines):
    is_puzzle = False
    puzzles = []
    key = -1
    puzzle = [] 

    for line in lines:
        if line.startswith('Tile'):
            key = int(line[5:-2])
            is_puzzle = True
            puzzle = []
            continue
        elif line.isspace():
            puzzles.append(Puzzle(puzzle, key))
            is_puzzle = False
            key = -1
            continue

        if is_puzzle:
            puzzle.append([el for el in line.strip()])

    return puzzles


def solved20(input_data):
    puzzles = extract_puzzles(input_data)

    board = Board(puzzles)
    board.solve_board()
    #print(board.show_board_as_string())

    hunter = MonsterHunter(board.joint)
    print(hunter)
    hunter.rotate()
    print(hunter)
    hunter.flip()
    print(hunter)


    return -1

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d20_example.txt")
    real_data = utils.loadStringData("./data/d20_real.txt")

    test = solved20(test_data)
    #real = solved20(real_data)

    #print(utils.OUTPUT_STRING.format("example", test))
    #print(utils.OUTPUT_STRING.format("exercise", real))
