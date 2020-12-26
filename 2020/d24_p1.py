import utils

def decode_coords(line):
    values = {'e': (1,0), 'w': (-1,0), 'se':(0.5,-1), 'sw':(-0.5,-1), \
            'ne': (0.5,1), 'nw':(-0.5,1)}
    start = [0,0] 

    limit = len(line)
    __i = 0
    while __i < limit:
        coor = line[__i]
        if coor == 's' or coor == 'n':
            coor = line[__i:__i+2]
            __i += 2
        else:
            __i += 1

        move = values.get(coor)
        start[0] += move[0]
        start[1] += move[1]

    return (start[0], start[1])

def get_coords(lines):
    coords = {}
    for line in lines:
        coord = decode_coords(line.strip())
        value = coords.get(coord, 0) + 1
        coords.update({coord: value})

    return coords

def get_black_tiles(tiles):
    count = 0
    for key in tiles.keys():
        if tiles.get(key) % 2 == 1:
            count += 1

    return count

def solved24(input_data):
    tiles = get_coords(input_data)

    return get_black_tiles(tiles) 

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d24_example.txt")
    real_data = utils.loadStringData("./data/d24_real.txt")

    test = solved24(test_data)
    real = solved24(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
