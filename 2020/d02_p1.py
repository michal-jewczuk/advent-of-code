import utils

def validate_password(data_row):
    lower_treshold = data_row[0]
    upper_treshold = data_row[1]
    target_letter = data_row[2]
    password_string = data_row[3]

    password_table = transform_to_table(password_string)
    letter_count = password_table.count(target_letter)

    return letter_count >= lower_treshold and letter_count <= upper_treshold

def transform_to_table(data_string):
    letters = [letter for letter in data_string]
    letters.sort()

    return letters

def transform_single_row(row):
    raw_data = row.split(" ")
    tresholds = raw_data[0].split("-")
    letter = raw_data[1][0]

    return (int(tresholds[0]), int(tresholds[1]), letter, raw_data[2])

def solved02(input_data):
    transformed_data = [transform_single_row(row) for row in input_data] 
    count = 0

    for row in transformed_data:
        if validate_password(row):
            count += 1

    return count

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d02_example.txt")
    real_data = utils.loadStringData("./data/d02_real.txt")

    test = solved02(test_data)
    real = solved02(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
