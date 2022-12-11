class Monkey {
	int index
	List<Integer> items
	String operation
	int divisible
	Map<Boolean, Integer> outcomes
	int inspections
}

def monkeys = []
def input = new File('input.txt').text.split(/\n\n/)
input.eachWithIndex {it, i ->
	def parts = it.split(/\n/)
	def items = parts.find {it.contains('Starting items')}.split(':')*.trim()[1].split(',').collect {it.trim().toInteger()}.toList()
	def operation = parts.find {it.contains('Operation')}.split(':')[1].trim().split('new =')[1]
	def divisible = parts.find {it.contains('Test')}.split(':')[1].split(/\s/).last().toInteger()
	def outcomes = [(Boolean.TRUE): parts.find {it.contains('If true')}.split(':')[1].split(/\s/).last().toInteger()]
	outcomes[Boolean.FALSE] = parts.find {it.contains('If false')}.split(':')[1].split(/\s/).last().toInteger()
	monkeys << new Monkey(index: i, items: items, operation: operation, divisible: divisible, outcomes: outcomes)
}

20.times {
	monkeys.each {m ->
		while (m.items) {
			def i = m.items.pop()
			def result = Math.floor(evaluate(m.operation.replaceAll('old', i.toString())) / 3).toInteger()
			m.inspections++
			monkeys[m.outcomes[(result % m.divisible == 0)]].items << result
		}
	}
}

def byInspection = monkeys.sort {it.inspections}.reverse()
println byInspection[0].inspections * byInspection[1].inspections