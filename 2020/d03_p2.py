import utils

def calculate_number_of_trees_for_slope(slope, input_data):
    count = 0
    right = slope[0] 
    row = 0 

    copy = input_data.copy()
    copy.pop(0)

    for input_row in copy:
        row += 1
        if row % slope[1] != 0:
            continue

        forest = input_row.strip() * row 

        if forest[right * row // slope[1]] == '#':
            count += 1

    return count

def solved03(input_data):
    slopes = [(1, 1), (3, 1), (5, 1), (7, 1), (1, 2)]
    
    results = [calculate_number_of_trees_for_slope(slope, input_data) for slope in slopes]
    print(results)

    answer = 1
    for result in results:
        answer *= result

    return answer

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d03_example.txt")
    real_data = utils.loadStringData("./data/d03_real.txt")

    test = solved03(test_data)
    real = solved03(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
