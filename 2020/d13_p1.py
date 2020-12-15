import utils

def get_dep_time(input_data):
    return int(input_data[0].strip())

def get_busses(input_data):
    busses = []
    entries = input_data[1].strip().split(',')
    for entry in entries:   
        if entry != 'x':
            busses.append(int(entry))

    return busses

def get_next_bus(busses, dep_time):
    next_deps = {}
    for bus in busses:
        next_dep = bus - dep_time % bus
        next_deps.update({bus: next_dep})

    values = next_deps.values()
    closest_dep = [y for y in next_deps.values()]
    closest_dep.sort()
    closest_dep = closest_dep[0]

    for bus_id in next_deps.keys():
        if next_deps[bus_id] == closest_dep:
            return (bus_id, closest_dep)

def get_result(data):
    return data[0] * data[1]

def solved13(input_data):
    dep_time = get_dep_time(input_data)
    busses = get_busses(input_data)
    next_bus = get_next_bus(busses, dep_time)
    print(next_bus)

    return get_result(next_bus) 

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d13_example.txt")
    real_data = utils.loadStringData("./data/d13_real.txt")

    test = solved13(test_data)
    real = solved13(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
