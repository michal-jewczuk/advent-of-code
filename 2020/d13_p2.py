import utils

def get_dep_times(input_data):
    entries = input_data[1].strip().split(',')
    dep_times = []
    time = 0

    for entry in entries:
        if entry == 'x':
            time += 1
            continue
        dep_times.append((int(entry), time))
        time += 1

    return dep_times

def get_start_point(start, jump):
    idx = 0
    while True:
        if (start + idx) % jump == 0:
            return start + idx
        idx += 1

def calculate_time(dep_times, start_point):
    jump = dep_times[0][0]
    t = get_start_point(start_point, jump)

    for dep_time in dep_times[1:]:
        while (t + dep_time[1]) % dep_time[0] != 0:
            t += jump
        jump *= dep_time[0]

    return t

def solved13(input_data, start_point):
    dep_times = get_dep_times(input_data)

    return calculate_time(dep_times, start_point)

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d13_example.txt")
    real_data = utils.loadStringData("./data/d13_real.txt")

    test = solved13(test_data, 0)
    real = solved13(real_data, 100000000000000)
    
    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
