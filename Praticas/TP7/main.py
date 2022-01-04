import networkx as nx
import matplotlib.pyplot as plt
import random

def generateRandomEdge(n_nodes, edges):
    node1 = random.randint(0, n_nodes)
    node2 = random.randint(0, n_nodes)

    edge = (node1, node2)

    if node1 == node2 or edge in edges:
        edge = generateRandomEdge(n_nodes, edges)

    return edge

def createGraph(n_nodes):
    g = nx.Graph()
    g.add_nodes_from(range(n_nodes))

    while not nx.is_connected(g):
        edge = generateRandomEdge(n_nodes, g.edges)
        g.add_edge(*edge)

    # subax1 = plt.subplot(121)
    # nx.draw(g, with_labels=True, font_weight='bold')
    # plt.show()

    return g.number_of_edges()


def getAverage(res):
    return sum(res) / len(res)


def getGraph():
    lGraphs = []
    lAverages = []
    lSize = []
    for i in range(1, 500, 10):
        for j in range(10):
            lGraphs.append(createGraph(i))
        lAverages.append(getAverage(lGraphs))
        lSize.append(i)

    plt.plot(lSize, lAverages)
    plt.show()

if __name__ == '__main__':
    getGraph()
