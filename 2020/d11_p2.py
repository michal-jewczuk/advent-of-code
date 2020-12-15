import utils


def transform_seat(seat):
    if seat == ".":
        return 0
    elif seat == "L":
        return 1
    return -1

def get_initial_grid(initial_data):
    output = []

    for line in initial_data:
        row = [transform_seat(seat) for seat in line.strip()]
        output.append(row)

    return output

def count_taken_seats_wide(coords, grid):
    offsets = [(-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1)]
    count = 0 

    for offset in offsets:
        if is_first_seat_taken(coords, grid, offset):
            count += 1

    return count

def is_in_bounds(grid, row, col):
    row_size = len(grid)
    col_size = len(grid[0])

    return (row >= 0 and row < row_size) and (col >= 0 and col < col_size)

def is_first_seat_taken(coords, grid, offset):
    row = coords[0]
    col = coords[1]

    while True:
        row += offset[0]
        col += offset[1]
        if not is_in_bounds(grid, row, col):
            return False
        
        if grid[row][col] == 0:
            continue

        return grid[row][col] == 2

def should_seat_change_state(coords, grid):
    seat = grid[coords[0]][coords[1]]

    if seat == 0:
        return False
    elif seat == 1:
        return count_taken_seats_wide(coords, grid) == 0
    elif seat == 2:
        return count_taken_seats_wide(coords, grid) > 4
    else:
        return False

def change_state(coords, grid):
    state = grid[coords[0]][coords[1]]
    new_state = 1
    if state == 1:
        new_state = 2

    grid[coords[0]][coords[1]] = new_state
    
    return grid

def run_iteration(grid):
    output = copy_list(grid)
    is_changed = False

    row = 0
    for row_el in grid:
        col = 0
        for col_el in row_el:
            if should_seat_change_state((row, col), grid):
                is_changed = True
                output = change_state((row, col), output)
            col += 1
        row += 1

    return (is_changed, output)

def copy_list(list):
    copy = []
    for row in list:
        copy.append([el for el in row])

    return copy

def count_occupied_seats(grid):
    count = 0

    for row in grid:
        for col in row:
            if col == 2:
                count += 1

    return count

def solved11(input_data):
    initial_grid = get_initial_grid(input_data)

    is_changed, output = run_iteration(initial_grid)

    while True:
        is_changed, output = run_iteration(output)
        if not is_changed:
            break

    return count_occupied_seats(output)

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d11_example.txt")
    real_data = utils.loadStringData("./data/d11_real.txt")

    test = solved11(test_data)
    real = solved11(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
