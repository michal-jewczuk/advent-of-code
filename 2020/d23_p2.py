import utils
import datetime

class Node:
    def __init__(self, data):
        self.data = data
        self.next = None
        # based on value not node 
        self.prev = None
        return

class ClockList:
    def __init__(self):
        self.head = None
        self.tail = None
        self.rhead = None
        self.rtail = None
        return

    def get_by_value(self, value):
        current = self.head
        while current.data != value:
            current = current.next

        return current

    def add_list_item(self, item):
        if not isinstance(item, Node):
            item = Node(item)

        if self.head is None:
            self.head = item
        else:
            self.tail.next = item
            item.next = self.head

        self.tail = item

    def assign_prev_values(self):
        current = self.head

        while current != self.tail:
            if current.data == 1:
                current.prev = self.tail
            elif current.data > 1 and current.data < 10:
                current.prev = self.get_by_value(current.data - 1)
            elif current.data == 10:
                current.prev = self.get_by_value(9)
                tmp = current.next
                tmp.prev = current
            else:
                tmp = current.next
                tmp.prev = current

            current = current.next

    def remove_items(self, start, count):
        tmp = start.next
        self.rhead = tmp
        self.tail = start

        for __i in range(count - 1):
            tmp = tmp.next

        self.rtail = tmp
        self.head = tmp.next
        self.tail.next = self.head

    def insert_items(self, start):
        tmp = start.next
        start.next = self.rhead 
        self.rtail.next = tmp
        if tmp == self.head:
            self.tail = self.rtail 

        self.rhead = None
        self.rtail = None

    def is_node_removed(self, target):
        current = self.rhead

        while current is not self.rtail:
            if current == target:
                return True
            current = current.next

        return self.rtail == target
    
    def get_next_lowest(self, start):
        tmp = start.prev
        while self.is_node_removed(tmp):
            tmp = tmp.prev

        return tmp

    def get_next_values(self, start, count):
        current = self.head

        while current.data != start:
            current = current.next

        output = []
        for i in range(2):
            output.append(current.next.data)
            current = current.next

        return output

    def __repr__(self):
        current = self.head
        output = '' 

        while current is not self.tail:
            output += '-' + str(current.data)
            current = current.next

        output += '-' + str(self.tail.data)

        return output 


def create_data(input_data):
    basic = [int(el) for el in input_data]
    data = ClockList()

    for el in basic:
        data.add_list_item(el)

    for el in range(len(basic) + 1, 1000001, 1):
        data.add_list_item(el)

    data.assign_prev_values()

    return data

def play_game(data, times):
    current = data.get_by_value(3)

    for __i in range(times):
        if __i % 1000000 == 0:
            print(datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"), 'done', __i)
        data.remove_items(current, 3)
        dest = data.get_next_lowest(current)
        data.insert_items(dest)
        current = current.next


def solved23(input_data):
    data = create_data(input_data)
    play_game(data, 10000000)
    result = data.get_next_values(1, 2)
    print(result)

    return result[0] * result[1] 

if __name__ == '__main__':
    test = solved23('389125467')
    real = solved23('362981754')

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
