import utils

def extract_directions(input_data):
    directions = []
    for line in input_data:
        direction = line[0]
        value = int(line[1:].strip())
        directions.append((direction, value))

    return directions

def count_target_coord(directions):
    ship = [0,0]
    waypoint = [10, 1]
    current_front = 'E'
    
    for direction in directions:
        command = direction[0]
        if command == "R" or command == "L":
            waypoint = rotate_waypoint(direction, waypoint)
        elif command == "F":
            ship = move_ship(direction[1], waypoint, ship)
        else:
            waypoint = move_waypoint(direction[0], direction[1], waypoint)

    return ship 

def rotate_waypoint(direction, waypoint):
    value = direction[0] + str(direction[1])
    if direction[1] == 360:
        return waypoint
    elif direction[1] == 180:
        hor = -1 * waypoint[0]
        ver = -1 * waypoint[1]
    elif value == "R90" or value == "L270":
        hor = waypoint[1]
        ver = -1 * waypoint[0]
    else:
        hor = -1 * waypoint[1]
        ver = waypoint[0]

    waypoint[0] = hor
    waypoint[1]= ver

    return waypoint

def move_waypoint(direction, value, target):
    if direction == "S" or direction == "W":
        value *= -1

    if direction == "N" or direction == "S":
        target[1] = target[1] + value
    else:
        target[0] = target[0] + value

    return target

def move_ship(value, waypoint, ship):
    ship[0] = ship[0] + waypoint[0] * value
    ship[1] = ship[1] + waypoint[1] * value

    return ship

def get_manhattan_distance(coords):
    return abs(coords[0]) + abs(coords[1])

def solved12(input_data):
    directions = extract_directions(input_data)
    target = count_target_coord(directions)
    print(target)

    return get_manhattan_distance(target) 

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d12_example.txt")
    real_data = utils.loadStringData("./data/d12_real.txt")

    test = solved12(test_data)
    real = solved12(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
