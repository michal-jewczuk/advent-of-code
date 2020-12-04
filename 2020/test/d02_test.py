import unittest
import sys
sys.path.append('../')

import d02

class TestValidate(unittest.TestCase):

    def test_valid_datas(self):
        testData = [
                [(1, 3, 'a', 'b'), False],
                [(2, 4, 'z', 'zacdzz'), True],
                [(0, 3, 'x', 'abcd'), True],
                [(0, 3, 'x', 'xxxx'), False],
                [(5, 7, 'y', 'd'), False],
                [(3, 3, 'g', 'ggag'), True],
                [(3, 3, 'g', 'gggg'), False],
                [(3, 3, 'g', 'gahgggg'), False]
                ]
        for data in testData:
            with self.subTest(data=data):
                self.assertEqual(d02.validate_password(data[0]), data[1])

        self.assertEqual(1, 1)


if __name__ == '__main__':
    unittest.main()
