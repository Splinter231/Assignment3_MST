# 📘 Assignment 3 Analytical Report

**Topic:** Optimization of a City Transportation Network (MST)
**Student:** Sergey Chepurnenko, Group SE-2422
**Course:** DAA

---

## **1. Summary of Input Data and Algorithm Results**

In this experiment, both **Prim’s** and **Kruskal’s** algorithms were implemented in Java to find the **Minimum Spanning Tree (MST)** for multiple datasets of different sizes and densities.
The algorithms were executed on **30 graphs** divided into four categories:

| Category | Graphs | Vertices Range | Avg Vertices | Avg Edges | Example Graph IDs |
| -------- | ------ | -------------- | ------------ | --------- | ----------------- |
| Small    | 5      | 7–30           | ~20          | ~28       | 1–5               |
| Medium   | 10     | 75–296         | ~140         | ~280      | 6–15              |
| Large    | 10     | 557–997        | ~850         | ~2100     | 16–25             |
| X-Large  | 5      | 1481–2933      | ~2045        | ~6100     | 26–30             |

All graphs were **connected and undirected**, ensuring identical MST costs between both algorithms.

Below is a summary of the **average performance** per category (based on `mst_metrics.csv`):

| Category | Avg Prim Time (ms) | Avg Kruskal Time (ms) | Avg Prim Ops | Avg Kruskal Ops | Avg MST Cost |
| -------- | ------------------ | --------------------- | ------------ | --------------- | ------------ |
| Small    | **0.73**           | **0.30**              | 52           | 52              | 683          |
| Medium   | **0.33**           | **0.38**              | 517          | 515             | 3570         |
| Large    | **1.75**           | **1.94**              | 4043         | 4041            | 19000        |
| X-Large  | **3.75**           | **5.43**              | 11775        | 11727           | 40460        |

---

## **2. Comparison Between Prim’s and Kruskal’s Algorithms**

### **Theoretical Comparison**

| Feature                       | **Prim’s Algorithm**             | **Kruskal’s Algorithm**                                |
| ----------------------------- | -------------------------------- | ------------------------------------------------------ |
| **Approach**                  | Expands MST from a single vertex | Sorts all edges and connects components via Union-Find |
| **Data Structure**            | Priority Queue (Min-Heap)        | Disjoint Set (Union-Find)                              |
| **Time Complexity**           | O(E log V)                       | O(E log E) ≈ O(E log V)                                |
| **Space Complexity**          | O(V + E)                         | O(V + E)                                               |
| **Best Suited For**           | Dense graphs                     | Sparse graphs                                          |
| **Implementation Complexity** | Moderate                         | Simpler                                                |
| **Edge Sorting Needed**       | No                               | Yes                                                    |

### **Practical Comparison (based on results)**

| Observation                               | Prim’s Algorithm                               | Kruskal’s Algorithm            |
| ----------------------------------------- | ---------------------------------------------- | ------------------------------ |
| **Execution Speed (Small graphs)**        | Slightly slower due to priority queue overhead | Faster on small datasets       |
| **Execution Speed (Medium–Large graphs)** | Faster for dense graphs                        | Slightly slower due to sorting |
| **Scalability (X-Large)**                 | Performs better (3.7 ms vs 5.4 ms)             | Sorting cost increases with E  |
| **Operation Count**                       | Nearly identical                               | Nearly identical               |
| **MST Cost**                              | Identical                                      | Identical                      |

**Conclusion:**
Both algorithms yield **identical MST total costs**, confirming correctness.
**Prim’s algorithm** performs better on **larger and denser datasets**,
while **Kruskal’s** is slightly faster for **small or sparse graphs**.

---

## **3. Conclusions**

* **Correctness:** Both algorithms produce identical MSTs.
* **Performance:**

  * **Prim** outperforms on **dense** or **large** graphs due to efficient priority queue use.
  * **Kruskal** runs slightly faster on **small** or **sparse** networks because of simpler structure.
* **Complexity:** Both follow O(E log V), but Prim scales better with increasing edge density.
* **Recommendation:**

  * Use **Prim’s** for **urban dense transportation networks**.
  * Use **Kruskal’s** for **regional or sparse systems**.

---

## **4. Visual Representation (Bonus Section)**

To enhance the analysis, a **graph visualization module** was added.
Each graph’s MST was exported into a **Graphviz `.dot` format** (see `/visuals/` folder).
These files show:

* **Vertices** as circular nodes labeled N1, N2, etc.
* **MST edges** highlighted in red and bold (`penwidth=3.0`).
* The rest of the graph omitted for clarity (only MST structure shown).

**Example:**

```dot
graph G1 {
  label="Graph 1 (Prim MST Visualization)";
  N22 -- N1 [label="35", color="red", penwidth=3.0];
  N21 -- N22 [label="3", color="red", penwidth=3.0];
  N22 -- N8 [label="11", color="red", penwidth=3.0];
  ...
}
```

You can visualize any file online at:
🔗 [Graphviz Online Viewer](https://dreampuf.github.io/GraphvizOnline/)
or directly inside IntelliJ IDEA with the **Graphviz plugin**.

Includes:
`graph1_prim.dot`, `graph2_prim.dot`, `graph3_prim.dot`, `graph4_prim.dot`, `graph5_prim.dot`

---

## **5. References**

5. References

Programiz. Prim’s Algorithm Explained with Examples. Retrieved from https://www.programiz.com/dsa/prim-algorithm

Programiz. Kruskal’s Algorithm Explained with Examples. Retrieved from https://www.programiz.com/dsa/kruskal-algorithm

GeeksforGeeks. Difference between Prim’s and Kruskal’s Algorithm. Retrieved from https://www.geeksforgeeks.org/difference-between-prims-and-kruskals-algorithm/

Benchmark Results for MST Algorithms (mst_metrics.csv). 

---

## **6. Appendix (Example Result Record)**

**Graph 16 (large)**

* Vertices = 924, Edges = 2309
* Prim → Cost = 21310 | Time = 2.88 ms | Ops = 4442
* Kruskal → Cost = 21310 | Time = 3.23 ms | Ops = 4445

**Graph 28 (x-large)**

* Vertices = 2408, Edges = 7223
* Prim → Cost = 48479 | Time = 4.43 ms | Ops = 14047
* Kruskal → Cost = 48479 | Time = 7.51 ms | Ops = 14037


