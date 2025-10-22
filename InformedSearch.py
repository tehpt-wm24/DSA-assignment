from collections import deque
import heapq
import time
import timeit

class State():
    def __init__(self, left_m, left_c, boat, right_m, right_c):
        self.left_m = left_m
        self.left_c = left_c
        self.boat = boat
        self.right_m = right_m
        self.right_c = right_c
        self.parent = None
        self.g = 0 # cost from start
        self.h = 0 # heuristic
        self.f = 0 # total cost (g + h)

    def is_goal(self):
        return self.left_m == 0 and self.left_c == 0 and self.right_m == 3 and self.right_c == 3
    
    def is_valid(self):
        if self.left_m >= 0 and self.right_m >= 0 and self.left_c >= 0 and self.right_c >= 0 and (self.left_m == 0 or self.left_m >= self.left_c) and (self.right_m == 0 or self.right_m >= self.right_c):
            return True
        else:
            return False
    
    def __eq__(self, other):
        return (self.left_m == other.left_m and self.left_c == other.left_c and self.boat == other.boat and self.right_m == other.right_m and self.right_c == other.right_c)
    
    def __hash__(self):
        return hash((self.left_m, self.left_c, self.boat, self.right_m, self.right_c))
    
    def __lt__(self, other):
        return self.f < other.f
    
    def heuristic(self):
        # Heuristic: number of people on the left side
        return self.left_m + self.left_c
    
def successors(cur_state):
    children = []
    # Two missionaries cross river
    # Two cannibals cross rriver
    # One missionary and one cannibal cross river
    # One missionary cross river
    # One cannibal cross river
    moves = [(2, 0), (0, 2), (1, 1), (1, 0), (0, 1)]

    for m, c in moves:
        if cur_state.boat == 'left':
            if cur_state.left_m >= m and cur_state.left_c >= c:
                new_state = State(cur_state.left_m - m, cur_state.left_c - c, 'right', cur_state.right_m + m, cur_state.right_c + c)
            else:
                continue
        else:
            if cur_state.right_m >= m and cur_state.right_c >= c:
                if m > 0 and cur_state.right_m == 3: # Cannot move missionaries back once they all are at right
                    continue
                new_state = State(cur_state.left_m + m, cur_state.left_c + c, 'left', cur_state.right_m - m, cur_state.right_c - c)
            else:
                continue
        
        if new_state.is_valid():
            new_state.parent = cur_state
            new_state.g = cur_state.g + 1
            new_state.h = new_state.heuristic()
            new_state.f = new_state.g + new_state.h
            children.append(new_state)

    return children
        
def reconstruct_path(cur_state):
    path = []
    while cur_state is not None:
        path.append(cur_state)
        cur_state = cur_state.parent
    return path[::-1]
        
def a_star_search(initial_state, collect_stats = False):
    open_list = []
    heapq.heappush(open_list, initial_state)

    cost_so_far = {initial_state: 0}
    nodes_expanded = 0
    max_open_list_size = 1

    if collect_stats:
        start_time = time.perf_counter()

    while open_list:
        cur_state = heapq.heappop(open_list)
        nodes_expanded += 1

        if collect_stats:
            max_open_list_size = max(max_open_list_size, len(open_list))

        if cur_state.is_goal():
            if collect_stats:
                end_time = time.perf_counter()
                execution_time = (end_time - start_time) * 1000 # Convert in milliseconds
                return reconstruct_path(cur_state), nodes_expanded, len(cost_so_far), max_open_list_size, execution_time
            else:
                return reconstruct_path(cur_state)
            
        for successor in successors(cur_state):
            new_cost = cur_state.g + 1 # All moves cost 1

            if successor not in cost_so_far or new_cost < cost_so_far[successor]:
                cost_so_far[successor] = new_cost
                successor.g = new_cost
                successor.h = successor.heuristic()
                successor.f = successor.g + successor.h
                successor.parent = cur_state
                heapq.heappush(open_list, successor)

    return None # No solution found
    
def print_state(state):
    print(f"[Left] -> Missionaries: {state.left_m}  Cannibals: {state.left_c} | Boat: {state.boat} | [Right] -> Missionaries: {state.right_m}  Cannibals: {state.right_c}")

def print_solution(path):
    if not path:
        print("No solution found!")
        return
    
    print("\nSolution:")
    for i, state in enumerate(path):
        if i == 0:
            print("\nInitial State:")
        else:
            print(f"\nStep {i}:")
        print_state(state)

def main():
    initial_state = State(3, 3, 'left', 0, 0)

    # Run once with detailed statistics
    print("\n" + "=" * 100)
    print("\t\tSolution of Missionaries and Cannibals: ")
    print("\nGOAL: Get all missionaries and cannibals to the right bank.")
    print("\nRULES:")
    print("\n\t1. The boat can carry at most 2 people")
    print("\n\t2. Cannibals cannot outnumber missionaries on either bank")
    print("\n" + "=" * 100)
    print("Running A* Search with detailed performance metrics...\n")
    
    result = a_star_search(initial_state, collect_stats = True)
    
    if result:
        solution_path, nodes_expanded, total_nodes_generated,  max_open_list_size, execution_time_ms = result
        
        print_solution(solution_path)
        
        print("\n" + "=" * 100)
        print("\t\t\t\tPERFORMANCE STATISTICS")
        print("=" * 100)
        print(f"Execution time: {execution_time_ms:.6f} ms")
        print(f"Solution Length: {len(solution_path) - 1} steps")
        print(f"Nodes Expanded: {nodes_expanded}")
        print(f"Total Nodes Generated: {total_nodes_generated}")
        print(f"Maximum Open List Size: {max_open_list_size}")
        print(f"Average Time per Node: {execution_time_ms/nodes_expanded:.6f} ms/node")

        # Calculate branching factor approximation
        if nodes_expanded > 1:
            depth = len(solution_path) - 1
            approx_branching_factor = total_nodes_generated ** (1/depth)
            print(f"Approximate Effective Branching Factor: {approx_branching_factor:.2f}")
    else:
        print("No solution found!")
        return
        
    print("\n" + "=" * 65)
    print("\tBENCHMARKING (1000 runs for accurate timing)")
    print("=" * 65)

    # Benchmark with timeit for accurate timing
    def run_benchmark():
        initial_state = State(3, 3, 'left', 0, 0)
        path = a_star_search(initial_state, collect_stats=False)
    
    # Time the execution
    time_taken = timeit.timeit(run_benchmark, number = 1000)
    average_time_ms = (time_taken / 1000) * 1000 # Convert to milliseconds per run
    
    print(f"Average execution time over 1000 runs: {average_time_ms:.6f} milliseconds")
    print(f"Total time for 1000 runs: {time_taken:.6f} seconds")
    
    if average_time_ms > 0:
        nodes_per_sec = nodes_expanded / (average_time_ms / 1000)
        print(f"Estimated nodes processed per second: {nodes_per_sec:.0f}")

if __name__ == "__main__":
    main()