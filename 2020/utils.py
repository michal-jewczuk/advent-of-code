OUTPUT_STRING = 'The result for {} data is: {}'

def loadStringData(input_file):
    with open(input_file, "r") as f:
        data = f.readlines()

    return data

def loadNumericData(input_file):
    data = loadStringData(input_file)

    return [int(d) for d in data]
