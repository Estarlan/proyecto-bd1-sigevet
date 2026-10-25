# Created: 18:55 24-Oct-2026
# Updated: 18:55 24-Oct-2026
# Updated: 18:55 24-Oct-2026
"""Orthogonal A* routes with clearance around every diagram vertex."""
from __future__ import annotations

import heapq
import math


def pairwise(values):
    """Adjacent pairs, including on the project's macOS Python 3.9 runtime."""
    left, right = iter(values), iter(values)
    next(right, None)
    return zip(left, right)


class Router:
    """Route through a coarse grid; endpoint stubs remain outside other boxes."""

    def __init__(self, boxes: dict, width: int, height: int, step: int = 20):
        self.boxes, self.step = boxes, step
        self.limit = (width // step - 2, height // step - 2)
        self.blocked, self.used = set(), {}
        for x, y, w, h in boxes.values():
            for gx in range(math.floor((x - 40) / step), math.ceil((x + w + 40) / step) + 1):
                for gy in range(math.floor((y - 40) / step), math.ceil((y + h + 40) / step) + 1):
                    self.blocked.add((gx, gy))

    def _ports(self, key: str) -> list[tuple]:
        x, y, w, h = self.boxes[key]
        s = self.step
        result = []
        for side, actual, outer in [
            ((0, .5), (x, y + h / 2), (math.floor((x - 60) / s) * s, y + h / 2)),
            ((1, .5), (x + w, y + h / 2), (math.ceil((x + w + 60) / s) * s, y + h / 2)),
            ((.5, 0), (x + w / 2, y), (x + w / 2, math.floor((y - 60) / s) * s)),
            ((.5, 1), (x + w / 2, y + h), (x + w / 2, math.ceil((y + h + 60) / s) * s)),
        ]:
            grid = (round(outer[0] / s), round(outer[1] / s))
            snapped = (grid[0] * s, grid[1] * s)
            stub = [actual, outer, snapped]
            if grid not in self.blocked and self.clear(stub, {key}):
                result.append((grid, side, stub))
        return result

    def clear(self, points: list, excluded: set) -> bool:
        """Check exact centerline intersection against unrelated rectangles."""
        for a, b in pairwise(points):
            for key, (x, y, w, h) in self.boxes.items():
                if key in excluded:
                    continue
                if a[0] == b[0] and x < a[0] < x + w and max(min(a[1], b[1]), y) < min(max(a[1], b[1]), y + h):
                    return False
                if a[1] == b[1] and y < a[1] < y + h and max(min(a[0], b[0]), x) < min(max(a[0], b[0]), x + w):
                    return False
        return True

    def route(self, source: str, target: str) -> tuple[list, tuple, tuple]:
        starts, ends = self._ports(source), self._ports(target)
        goals = {port[0]: port for port in ends}
        if not starts or not goals:
            raise ValueError(f"No free route ports: {source} -> {target}")
        def heuristic(point):
            return min(abs(point[0] - g[0]) + abs(point[1] - g[1]) for g in goals)
        queue, costs, previous, origins = [], {}, {}, {}
        for port in starts:
            state = (*port[0], -1)
            costs[state], origins[state] = 0, port
            heapq.heappush(queue, (heuristic(port[0]), 0, state))
        while queue:
            _, cost, state = heapq.heappop(queue)
            if cost != costs.get(state):
                continue
            x, y, direction = state
            if (x, y) in goals:
                route = []
                while state in previous:
                    route.append((state[0] * self.step, state[1] * self.step))
                    state = previous[state]
                start, finish = origins[state], goals[(x, y)]
                points = start[2] + list(reversed(route)) + list(reversed(finish[2]))
                points = self._simplify(points)
                if not self.clear(points, {source, target}):
                    raise ValueError(f"Route intersects an unrelated box: {source} -> {target}")
                for point in route:
                    grid = (round(point[0] / self.step), round(point[1] / self.step))
                    self.used[grid] = self.used.get(grid, 0) + 1
                return points, start[1], finish[1]
            for turn, (dx, dy) in enumerate(((1, 0), (0, 1), (-1, 0), (0, -1))):
                point = (x + dx, y + dy)
                if point in self.blocked or not (2 <= point[0] <= self.limit[0] and 7 <= point[1] <= self.limit[1]):
                    continue
                next_state = (*point, turn)
                next_cost = cost + 1 + (2 if direction not in (-1, turn) else 0) + self.used.get(point, 0) * 2
                if next_cost < costs.get(next_state, float("inf")):
                    costs[next_state], previous[next_state] = next_cost, state
                    heapq.heappush(queue, (next_cost + heuristic(point), next_cost, next_state))
        raise ValueError(f"No obstacle-free route: {source} -> {target}")

    @staticmethod
    def _simplify(points: list) -> list:
        result = []
        for point in points:
            if result and result[-1] == point:
                continue
            while len(result) > 1 and ((result[-2][0] == result[-1][0] == point[0]) or (result[-2][1] == result[-1][1] == point[1])):
                result.pop()
            result.append(point)
        return result
