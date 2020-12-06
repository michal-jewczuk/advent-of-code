import utils

def transform_input_data(input_data):
    data = []
    answers_line = ''

    for line in input_data:
        if line.isspace():
           data.append(set(answers_line.strip()))
           answers_line = ''
        else:
           answers_line += line.strip()

    data.append(set(answers_line.strip()))

    return data


def solved06(input_data):
    count = 0

    answers_set = transform_input_data(input_data)
    for answer in answers_set:
        count += len(answer)

    return count 

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d06_example.txt")
    real_data = utils.loadStringData("./data/d06_real.txt")

    test = solved06(test_data)
    real = solved06(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
