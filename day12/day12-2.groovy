def input = new File('input.txt').text.split(/\n/)
def edges = [
		[0, -1],
		[0, 1],
		[-1, 0],
		[1, 0]
]
def start, end
def y = 0
def map = input.collect {row ->
	def x = 0
	def r = row.collect {ch ->
		def node = [
				x       : x++,
				y       : y,
				visited : false,
				height  : ch.toCharacter() - 96,
				distance: Integer.MAX_VALUE
		]
		if (ch == 'S') {
			node.height = 1
			start = node
		}
		if (ch == 'E') {
			node.height = 26
			end = node
		}
		return node
	}
	y++
	r
}

map.each {r ->
	r.each {node ->
		def nodes = []
		for (def edge in edges) {
			if (!map[node.y + edge[1]] || node.y + edge[1] < 0) continue
			def n = map[node.y + edge[1]][node.x + edge[0]]
			if (n) nodes << n
		}
		node.edgeNodes = nodes
	}
}

def solve = {n ->
	n.distance = 0
	def queue = [n]
	while (queue) {
		def node = queue.pop()
		for (def edge in node.edgeNodes) {
			if (!edge.visited && (node.height - edge.height) < 2) {
				def distance = node.distance + 1
				if (edge.x == 0) {
					return distance
				} else {
					edge.visited = true
					edge.distance = distance
					queue << edge
				}
			}
		}
	}
	return null
}

println solve(end)