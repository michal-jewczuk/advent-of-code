import utils

def transform_input_data(input_data):
    data = []
    people = []

    for line in input_data:
        if line.isspace():
            data.append(people)
            people = []
        else:
            people.append(set(line.strip()))
           

    data.append(people)

    return data


def solved06(input_data):
    count = 0

    answers_set = transform_input_data(input_data)
    for answer in answers_set:
        if len(answer) == 1:
            count += len(answer[0])
        else: 
            tmp = answer[0].intersection(*answer[1:])
            count += len(tmp)

    return count 

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d06_example.txt")
    real_data = utils.loadStringData("./data/d06_real.txt")

    test = solved06(test_data)
    real = solved06(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
