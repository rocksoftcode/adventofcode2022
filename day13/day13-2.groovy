import groovy.json.JsonSlurper

def input = new File('input.txt').text.split(/\n\n/)*.split(/\n/)*.collect {new JsonSlurper().parseText(it)}
def compare
def isList = {it instanceof List}
compare = {l, r ->
	if ([l, r].every {!isList(it)}) return l - r
	else {
		if (!isList(l)) l = [l]
		if (!isList(r)) r = [r]
		for (def i = 0; i < Math.min(l.size(), r.size()); i++)
			if ((c = compare(l[i], r[i])) != 0) return c
		return l.size() - r.size()
	}
}
input << [[[2]], [[6]]]
def sorted = input.collectMany {it}.sort {a, b -> compare(a, b)}
println((sorted.indexOf([[2]]) + 1) * (sorted.indexOf([[6]]) + 1))