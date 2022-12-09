class Node {
	int x
	int y
	int id
	Node child
	Node parent

	Node(int x, int y, int id, Node child) {
		this.x = x
		this.y = y
		this.id = id
		this.child = child
		this.parent = null
	}
}

def build = {len ->
	Node prev = null
	Node node = null
	(0..len-1).each {
		node = new Node(0, 0, it, prev)
		if (prev) prev.parent = node
		prev = node
	}
	return node
}

def move = {node ->
	def diffX = node.x - node.parent.x
	def diffY = node.y - node.parent.y
	if (diffX > 1) {
		node.x -= 1
		node.y = Math.abs(diffY) == 1 ? node.parent.y : node.y
	} else if (diffX < -1) {
		node.x += 1
		node.y = Math.abs(diffY) == 1 ? node.parent.y : node.y
	}
	if (diffY > 1) {
		node.y -= 1
		node.x = Math.abs(diffX) == 1 ? node.parent.x : node.x
	} else if (diffY < -1) {
		node.y += 1
		node.x = Math.abs(diffX) == 1 ? node.parent.x : node.x
	}
}

def pull = {head, dir, len, positions ->
	len.times {
		switch (dir) {
			case 'U': head.y -= 1; break
			case 'D': head.y += 1; break
			case 'R': head.x += 1; break
			case 'L': head.x -= 1; break
		}
		def node = head.child
		while (node) {
			move(node)
			if (!node.child) {
				def pos = node.x + "," + node.y
				positions[pos] = 1
			}
			node = node.child
		}
	}
}

def input = new File('input.txt').text.split(/\n/)
def positions = [:]
def head = build(10)
input.each {
	if (it.length() > 0) {
		def inst = it.split(/\s/)
		pull(head, inst[0], inst[1].toInteger(), positions)
	}
}
println positions.size()