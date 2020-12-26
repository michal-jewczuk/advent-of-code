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

def should_be_flipped(tile, tiles, not_in_list):
    adject = [(1,0), (-1,0), (0.5,1), (-0.5,1), (0.5,-1), (-0.5,-1)]
    neigh = [(el[0] + tile[0], el[1] + tile[1]) for el in adject]
    black = 0
    for __n in neigh:
        count = tiles.get(__n)
        if count == None:
            not_in_list.add(__n)
        elif count % 2 == 1:
            black += 1

    colour = tiles.get(tile, 0) % 2

    if colour == 1:
        return black == 0 or black > 2
    else:
        return black == 2

def get_tiles_to_flip(tiles):
    to_flip = []
    not_in_list = set()
    for tile in tiles:
        if should_be_flipped(tile, tiles, not_in_list):
            to_flip.append(tile)

    if len(not_in_list) > 0:
        for tile in not_in_list:
            if should_be_flipped(tile, tiles, set()):
                to_flip.append(tile)

    return to_flip 

def flip_round(to_flip, tiles):
    for tile in to_flip:
        value = tiles.get(tile, 0) + 1
        tiles.update({tile:value})

def flip_tiles(count, tiles):
    for __i in range(count):
        tiles_to_flip = get_tiles_to_flip(tiles)
        flip_round(tiles_to_flip, tiles)

def solved24(input_data):
    tiles = get_coords(input_data)
    flip_tiles(100, tiles)

    return get_black_tiles(tiles) 

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d24_example.txt")
    real_data = utils.loadStringData("./data/d24_real.txt")

    test = solved24(test_data)
    real = solved24(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
