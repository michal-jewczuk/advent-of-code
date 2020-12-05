import utils
import math

def get_range(input_range, is_upper):
    lower = input_range[0]
    upper = input_range[1]

    if lower == upper:
        return input_range

    middle = (lower + upper) / 2

    if is_upper:
        return (math.ceil(middle), upper)
    else:
        return (lower, math.floor(middle))

def is_upper(direction):
    return (direction == 'B' or direction == 'R')

def extract_rows_columns(rowscols):
    rows = rowscols[0:7]
    cols = rowscols[7:]
    return (rows.strip(), cols.strip())

def get_row_col(coord, is_row):
    starting_row_range = (0, 127)
    starting_col_range = (0, 7)
    current_range = starting_row_range if is_row else starting_col_range 

    for letter in coord:
        current_range = get_range(current_range, is_upper(letter))

    return current_range[0]

def get_seat_id(coords):
    (row_coord, col_coord) = extract_rows_columns(coords)
    seat_row = get_row_col(row_coord, True)
    seat_col = get_row_col(col_coord, False)
    seat_id = seat_row * 8 + seat_col
    #print(coords, seat_row, seat_col, seat_id)

    return seat_id

def solved05(input_data):
    seat_ids = [get_seat_id(coords) for coords in input_data]
    seat_ids.sort()
    number_of_seats = len(seat_ids)
    index = 0

    while index < number_of_seats:
        if seat_ids[index + 1] - seat_ids[index] != 1:
            return seat_ids[index] + 1
        index += 1

    return -1

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d05_example.txt")
    real_data = utils.loadStringData("./data/d05_real.txt")

    test = solved05(test_data)
    real = solved05(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
