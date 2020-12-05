import unittest
import sys
sys.path.append('../')

import d05_p1 

class TestD05(unittest.TestCase):

    def test_ranges(self):
        testData = [
                [(1,1), True, (1,1)],
                [(1,1), False, (1,1)],
                [(0,3), False, (0,1)],
                [(0,3), True, (2,3)],
                [(40,47), True, (44,47)]
                ]
        for data in testData:
            with self.subTest(data=data):
                self.assertEqual(d05_p1.get_range(data[0], data[1]), data[2])

    def test_seats(self):
        testData = [
                ['LLL', False, 0],
                ['RRR', False, 7],
                ['RLR', False, 5],
                ['FFFFFFF', True, 0],
                ['BBBBBBB', True, 127],
                ['BBBBBBF', True, 126],
                ['FBBBBBF', True, 62],
                ['BBFBBBB', True, 111]
                ]
        for data in testData:
            with self.subTest(data=data):
                self.assertEqual(d05_p1.get_row_col(data[0], data[1]), data[2])

    def test_ids(self):
        testData = [
                ['BBFBBBBRLL', 892]
                ]
        for data in testData:
            with self.subTest(data=data):
                self.assertEqual(d05_p1.get_seat_id(data[0]), data[1])


if __name__ == '__main__':
    unittest.main()
