import utils

def findNumbers(numbers):
    for number in numbers:
        if number >= 2020:
            return -1

        target = 2020 - number
        try:
            numbers.index(target)
            return (number, target)
        except:
            continue

    return -1

def validateData(data):
    if len(data) < 2:
        raise ValueError("You need to have at least 2 elements")

    data.sort()

    return data.copy()

def solved01(numbers):
    copy = validateData(numbers)

    result = findNumbers(copy)

    if result == -1:
        raise ValueError("There were no 2 elements matching criteria")

    return result[0] * result[1]

if __name__ == '__main__':
    test_data = utils.loadNumericData("./data/d01_example.txt")
    real_data = utils.loadNumericData("./data/d01_real.txt")

    test = solved01(test_data)
    real = solved01(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
