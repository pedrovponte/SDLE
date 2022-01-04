import networkx as nx
import matplotlib.pyplot as plt
import random

def generateRandomEdge(n_nodes, g):
    edges = [len(g.edges(x)) + 1 for x in range(n_nodes)]
    edgesProb = [x / sum(edges) for x in edges]
    edges = [x for x in range(n_nodes)]
    node1 = random.choices(edges, edgesProb)
    node2 = random.choices(edges, edgesProb)

    edge = (node1[0], node2[0])

    if node1 == node2 or edge in g.edges:
        edge = generateRandomEdge(n_nodes, g)

    return edge

def createGraph(n_nodes):
    g = nx.Graph()
    g.add_nodes_from(range(n_nodes))

    while not nx.is_connected(g):
        edge = generateRandomEdge(n_nodes, g)
        g.add_edge(*edge)

    return g.number_of_edges()


def getAverage(res):
    return sum(res) / len(res)


def getGraph():
    lGraphs = []
    lAverages = []
    lSize = []
    for i in range(1,100):
        for j in range(10):
            lGraphs.append(createGraph(i))
        lAverages.append(getAverage(lGraphs))
        lSize.append(i)

    plt.plot(lSize, lAverages)
    plt.show()

if __name__ == '__main__':
    getGraph()
