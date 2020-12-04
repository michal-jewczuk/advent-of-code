import utils

def solved03(input_data):
    count = 0
    right = 3
    row = 1 

    input_data.pop(0)

    for input_row in input_data:
        forest = input_row.strip() * row 

        if forest[right * row] == '#':
            count += 1

        row +=1

    return count

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d03_example.txt")
    real_data = utils.loadStringData("./data/d03_real.txt")

    test = solved03(test_data)
    real = solved03(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
