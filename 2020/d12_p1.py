import utils

def extract_directions(input_data):
    directions = []
    for line in input_data:
        direction = line[0]
        value = int(line[1:].strip())
        directions.append((direction, value))

    return directions

def count_target_coord(directions):
    target = [0,0]
    current_front = 'E'
    
    for direction in directions:
        command = direction[0]
        if command == "R" or command == "L":
            current_front = rotate(direction, current_front)
        elif command == "F":
            target = move(current_front, direction[1], target)
        else:
            target = move(direction[0], direction[1], target)

    return target

def rotate(direction, front):
    fronts = ["N", "E", "S", "W"]
    current = fronts.index(front)
    shift = direction[1] // 90
    if direction[0] == "L":
        shift *= -1

    shift = (current + shift) % 4

    return fronts[shift]

def move(direction, value, target):
    if direction == "S" or direction == "W":
        value *= -1

    if direction == "N" or direction == "S":
        target[1] = target[1] + value
    else:
        target[0] = target[0] + value

    return target

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
