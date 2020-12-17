import utils

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

def add_z_layer(cube):
    size = len(cube[0])
    empty = [['.' for j in range(size)] for i in range(size)]
    new_idx = len(cube) 
    cube.update({new_idx: empty})
    
def get_neighbours_from_same_layer(row, col, layer):
    neighbours = []
    offsets = [(-1,-1), (-1,0), (-1,1), (0,-1), (0,1), (1,-1), (1,0), (1,1)]
    size = len(layer) - 1

    for offset in offsets:
        i = row + offset[0]
        j = col + offset[1]
        if (i > size or j > size):
            neighbours.append('.')
        else:
            neighbours.append(layer[i][j])

    return neighbours

def get_neighbours_from_offset_layers(row, col, cube, z):
    neighbours = []
    below = []
    above = []
    if z == len(cube) - 1:
        below = get_neighbours_from_same_layer(row, col, cube[z-1])
        below.append(cube[z-1][row][col])
    elif z == 0:
        above = get_neighbours_from_same_layer(row, col, cube[1])
        above.append(cube[1][row][col])
        below = above
    else:
        above = get_neighbours_from_same_layer(row, col, cube[z+1])
        above.append(cube[z+1][row][col])
        below = get_neighbours_from_same_layer(row, col, cube[z-1])
        below.append(cube[z-1][row][col])

    neighbours.extend(below)
    neighbours.extend(above)

    return neighbours 

def get_neighbours(row, col, z, cube):
    neighbours = get_neighbours_from_same_layer(row, col, cube[z])
    z_layers = get_neighbours_from_offset_layers(row, col, cube, z)
    neighbours.extend(z_layers)

    return neighbours 

def should_cell_change_state(state, bubble):
    active = bubble.count('#')

    if state == '.':
        return active == 3
    else:
        return (active < 2 or active > 3)


def get_cells_in_layer(cube, z):
    layer = cube[z]
    size = len(layer)
    changes = []
    for row in range(size):
        for col in range(size):
            bubble = get_neighbours(row, col, z, cube)
            if should_cell_change_state(layer[row][col], bubble):
                changes.append((row, col))

    return changes

def get_cells_with_state_change(cube):
    size = len(cube) - 1
    state_change = {}
    for z in range(size, -1, -1):
        cells_to_change = get_cells_in_layer(cube, z)
        state_change.update({z: cells_to_change})

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
    add_z_layer(new_cube)

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
    for i in range(1, len(cube)):
        active += get_active_cells_in_layer(cube[i])

    active *= 2
    active += get_active_cells_in_layer(cube[0])

    return active

def solved17(input_data):
    initial = transform_data(input_data)
    cube = {0: initial}

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
