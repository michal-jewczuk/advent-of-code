import utils
import math

def transform_data(input_data):
    return [list(line.strip()) for line in input_data]

def rewrite_data(old, new):
    size = len(old)
    for row in range(size):
        for col in range(size):
            new[row + 1][col + 1] = old[row][col]

    return new

def resize_single_layer(layer):
    size = len(layer) + 2
    empty_layer = [['.' for j in range(size)] for i in range(size)]
    new_layer = rewrite_data(layer, empty_layer)

    return new_layer

def resize_cube(data):
    resized = {}
    for key in data.keys():
        new_dim = resize_single_layer(data.get(key))
        resized.update({key: new_dim})

    return resized

def add_layers(cube):
    size = len(cube[(0,0)])
    hidx = (math.sqrt(len(cube)) + 1) // 2
    hidx = int(hidx)
    lidx = -1 * hidx
    all_dims = [(i,j) for j in range(lidx, hidx + 1, 1) for i in range(lidx, hidx + 1, 1)]

    for dim in all_dims:
        if cube.get(dim) == None:
            cube.update({dim: [['.' for j in range(size)] for i in range(size)]})

def get_neighbours_from_layer(row, col, layer):
    neighbours = []
    offsets = [(-1,-1), (-1,0), (-1,1), (0,-1), (0,1), (1,-1), (1,0), (1,1)]
    size = len(layer) - 1

    for offset in offsets:
        i = row + offset[0]
        j = col + offset[1]
        if (i > size or i < 0 or j > size or j < 0):
            neighbours.append('.')
        else:
            neighbours.append(layer[j][i])

    return neighbours

def get_neighbours(row, col, key, cube):
    neighbours = []
    offset_z = range(key[0] -1, key[0] + 2, 1)
    offset_w = range(key[1] -1, key[1] + 2, 1)

    offsets = [(z,w) for z in offset_z for w in offset_w]
    offsets.remove(key)

    for offset in offsets:
        if cube.get(offset) != None:
            result = get_neighbours_from_layer(row, col, cube.get(offset))
            neighbours.extend(result)
            neighbours.extend(cube[offset][col][row])

    same_layer = get_neighbours_from_layer(row, col, cube.get(key))
    neighbours.extend(same_layer)

    return neighbours 

def should_cell_change_state(state, bubble):
    active = bubble.count('#')

    if state == '.':
        return active == 3
    else:
        return (active < 2 or active > 3)

def get_cells(cube, key):
    layer = cube[key]
    size = len(layer)
    changes = []

    for row in range(size):
        for col in range(size):
            bubble = get_neighbours(row, col, key, cube)
            if should_cell_change_state(layer[col][row], bubble):
                changes.append((col, row))

    return changes

def get_cells_with_state_change(cube):
    state_change = {}
    for key in cube.keys():
        cells_to_change = get_cells(cube, key)
        state_change.update({key: cells_to_change})

    return state_change 

def apply_state_change(cells, cube):
    for key in cells.keys():
        for cell in cells[key]:
            state = cube[key][cell[0]][cell[1]]
            new_state = '#'
            if state == '#':
                new_state = '.'

            cube[key][cell[0]][cell[1]] = new_state

def run_cycle(data):
    new_cube = resize_cube(data)
    add_layers(new_cube)

    cells_to_change = get_cells_with_state_change(new_cube)
    apply_state_change(cells_to_change, new_cube)

    return new_cube 

def get_active_cells_in_layer(layer):
    active = 0
    for row in layer:
        active += row.count('#')

    return active

def get_active_cells(cube):
    active = 0
    for key in cube.keys():
        active += get_active_cells_in_layer(cube[key])

    return active

def solved17(input_data):
    initial = transform_data(input_data)
    cube = {(0,0): initial}

    for i in range(6):
        cube = run_cycle(cube)

    return get_active_cells(cube)

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d17_example.txt")
    real_data = utils.loadStringData("./data/d17_real.txt")

    test = solved17(test_data)
    real = solved17(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
