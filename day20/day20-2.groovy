def input = new File('input.txt').readLines()*.toBigInteger().toList()
def nodes = []
def add = {value ->
	nodes << [
			id       : nodes.size(),
			value    : value,
			markLeft : nodes.size() - 1,
			markRight: nodes.size() + 1
	]
}
def left = {nodeId -> nodes[nodeId].markLeft}
def right = {nodeId -> nodes[nodeId].markRight}
def values = {value -> nodes.findAll {n -> n.value == value}[0]}
def move = {node, moves ->
	def absMoves = Math.abs(moves) % (nodes.size() - 1)
	if (absMoves == 0) return
	def markLeft = node.markLeft
	def markRight = node.markRight
	def nodeId = node.id
	def dir = moves > 0 ? right : left
	absMoves.times {nodeId = dir(nodeId)}
	if (moves < 0) {
		node.markLeft = nodes[nodeId].markLeft
		node.markRight = nodeId
	} else {
		node.markLeft = nodeId
		node.markRight = nodes[nodeId].markRight
	}
	nodes[node.markLeft].markRight = node.id
	nodes[node.markRight].markLeft = node.id

	nodes[markLeft].markRight = markRight
	nodes[markRight].markLeft = markLeft
}
def mult = new BigDecimal("811589153")
input.each {n -> add(n * mult)}
nodes[0].markLeft = nodes.size() - 1
nodes[nodes.size() - 1].markRight = 0
10.times {nodes.each {n -> move(n, n.value)}}
def nodeId = values(0).id
println([1000, 1000, 1000].inject(0) {a, n ->
	n.times {nodeId = right(nodeId)}
	a + nodes[nodeId].value
})