import networkx as nx
fh = open("/Users/Lohith/Documents/NBCNewsData/edgeList.txt", 'rb')
G = nx.read_edgelist(fh, create_using=nx.DiGraph())
fh.close()

pr = nx.pagerank(G, alpha=0.85, personalization=None, max_iter=30, tol=1e-06, nstart=None, weight='weight', dangling=None)
print(nx.number_of_nodes(G))
f = open("/Users/Lohith/Documents/NBCNewsData/external_PageRankFile.txt", "a")
for i in pr.keys():
    f.write(str(i)+" = "+str(pr[i])+"\n")
