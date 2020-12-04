import unittest
import sys
sys.path.append('../')

import d01

class TestFindNumbers(unittest.TestCase):

    def test_with_only_one_matching_pair(self):
        testData = [10, 2010]
        expected = (10, 2010)

        result = d01.findNumbers(testData)

        self.assertEqual(result, expected)


    def test_with_two_matching_pairs(self):
        testData = [100, 350, 500, 1670, 1920]
        expected = (100, 1920)

        result = d01.findNumbers(testData)

        self.assertEqual(result, expected)


    def test_all_but_one_grater_than_2020(self):
        testData = [2000, 2021, 2023, 5000]
        expected = -1

        result = d01.findNumbers(testData)

        self.assertEqual(result, expected)


    def test_no_matching_elements(self):
        testData = [200, 221, 203, 5000]
        expected = -1

        result = d01.findNumbers(testData)

        self.assertEqual(result, expected)
    

    def test_with_elements_grater_than_2020(self):
        testData = [17, 323, 400, 420, 1600, 1810, 3000, 5000]
        expected = (420, 1600) 

        result = d01.findNumbers(testData)

        self.assertEqual(result, expected)


class TestValidateData(unittest.TestCase):

    def test_valid_data(self):
        testData = [3, 35, 1, -4, 77, 3000, 450]
        expected = [-4, 1, 3, 35, 77, 450, 3000]

        result = d01.validateData(testData)

        self.assertEqual(result, expected)


    def test_list_with_not_enough_elements(self):
        testData = [[], [2000]]
        expected = "You need to have at least 2 elements"

        for data in testData:
            with self.subTest(data=data):
                with self.assertRaises(ValueError) as cm:
                    d01.validateData(data)
                self.assertEqual(str(cm.exception), expected);


class TestSolution(unittest.TestCase):

    def test_example_data(self):
        testData = [1721, 979, 366, 299, 675, 1456]
        expected = 514579

        result = d01.solved01(testData)

        self.assertEqual(result, expected)

    def test_valid_data(self):
        testData = [
                ([1, 2019], 2019),
                ([1500, 20, 4, 800, 2000], 40000),
                ([351, 13, 1750, 888, 25, 1900, 270], 270*1750)
                ]

        for data in testData:
            with self.subTest(data=data):
                self.assertEqual(d01.solved01(data[0]), data[1])



if __name__ == '__main__':
    unittest.main()
