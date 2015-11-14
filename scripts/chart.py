#!/usr/bin/env python
import numpy as np
import matplotlib.pyplot as plt

with open ('results', 'r') as f:
    for y in range(0,10):
        f.readline()
        line = f.readline()
        total = line.split()[4]
        f.readline()
        fig = plt.figure()
        values = {}
        values["raw"] = int(total)

        for x in range (0,5):
            line = f.readline().split()
            values[line[8][line[8].index('_') + 1 :]] = int(line[4])
        plt.bar(range(len(values)), sorted(values.values()), align='center')
        plt.xticks(range(len(values)), sorted(values, key=values.get))
        plt.savefig("image" + str(y))
