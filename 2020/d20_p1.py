import utils

def extract_tiles(lines):
    tiles = {}
    is_tile = False
    key = -1

    for line in lines:
        if line.startswith('Tile'):
            key = int(line[5:-2])
            is_tile = True
            tile = []
            continue
        elif line.isspace():
            tiles.update({key: tile})
            is_tile = False
            key = -1
            continue

        if is_tile:
            tile.append([el for el in line.strip()])

    return tiles

def create_initial_puzzle(tiles):
    puzzles = {}
    for key in tiles.keys():
        puzzles.update({key: [-1 for __i in range(4)]})

    return puzzles

def extract_top_bottom(tile):
    top = ''.join([el for el in tile[0]])
    bottom = ''.join([el for el in tile[-1]])
    return top, bottom

def extract_right_left(tile):
    right = ''
    left = ''
    for __i in range(len(tile)):
        left += tile[__i][0]
        right += tile[__i][-1]

    return right, left

def extract_borders(tiles):
    borders = {}
    for key in tiles.keys():
        tile = tiles.get(key)
        top, bottom = extract_top_bottom(tile)
        right,left = extract_right_left(tile)
        borders.update({key: [top, right, bottom, left]})

    return borders

def flip_vertically(borders):
    borders[1] = borders[1][::-1]
    borders[3] = borders[3][::-1]

def flip_horizontally(borders):
    borders[0] = borders[0][::-1]
    borders[2] = borders[2][::-1]

def are_matching(origin, target):
    for i in range(4):
        for j in range(4):
            if origin[i] == target[j]:
                return (i,j)
    return (-1,-1)

def solve_one(tile, puzzles, borders):
    puzzle = puzzles.get(tile)
    border = borders.get(tile)

    for puzz in puzzles.keys():
        if puzz == tile:
            continue

        tmp_puzzle = puzzles.get(puzz)
        tmp_border = borders.get(puzz)

        result = are_matching(border, tmp_border)
        if result != (-1,-1):
            puzzle[result[0]] = puzz
            tmp_puzzle[result[1]] = tile
            continue

        flip_vertically(tmp_border)
        result = are_matching(border, tmp_border)
        if result != (-1,-1):
            puzzle[result[0]] = puzz
            tmp_puzzle[result[1]] = tile
            continue

        flip_horizontally(tmp_border)
        result = are_matching(border, tmp_border)
        if result != (-1,-1):
            puzzle[result[0]] = puzz
            tmp_puzzle[result[1]] = tile
            continue

def get_corners(puzzles):
    corners = []
    for key in puzzles.keys():
        if puzzles.get(key).count(-1) == 2:
            corners.append(key)

    return corners

def calculate_result(corners):
    result = 1
    for corner in corners:
        result *= corner

    return result

def solved20(input_data):
    tiles = extract_tiles(input_data)
    puzzles = create_initial_puzzle(tiles)
    borders = extract_borders(tiles)
    for key in puzzles.keys():
        solve_one(key, puzzles, borders)
    print(puzzles)

    corners = get_corners(puzzles)
    print(corners)

    return calculate_result(corners) 

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d20_example.txt")
    real_data = utils.loadStringData("./data/d20_real.txt")

    test = solved20(test_data)
    real = solved20(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
