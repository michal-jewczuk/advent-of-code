import utils

YEAR_NUMBER = 2020

def findNumbers(numbers, sum_target):
    for number in numbers:
        if number > sum_target:
            return -1

        target = sum_target - number
        try:
            numbers.index(target)
            return (number, target)
        except:
            continue

    return -1

def validateData(data):
    if len(data) < 3:
        raise ValueError("You need to have at least 3 elements")

    data.sort()

    return data.copy()

def solved01(numbers):
    copy = validateData(numbers)

    for number in numbers:
        tmp_sum = YEAR_NUMBER - number
        partial = findNumbers(numbers, tmp_sum)

        if partial != -1:
            return number * partial[0] * partial[1]


    raise ValueError("There were no 3 elements matching criteria")


if __name__ == '__main__':
    test_data = utils.loadNumericData("./data/d01_example.txt")
    real_data = utils.loadNumericData("./data/d01_real.txt")

    test = solved01(test_data)
    real = solved01(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
