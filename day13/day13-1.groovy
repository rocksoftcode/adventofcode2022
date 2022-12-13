import groovy.json.JsonSlurper

def input = new File('input.txt').text.split(/\n\n/)*.split(/\n/)*.collect {new JsonSlurper().parseText(it)}
def compare
def isList = {it instanceof List}
def sum = 0
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
input.eachWithIndex {it, i -> if (compare(it[0], it[1]) < 0) sum += i + 1}
println sum