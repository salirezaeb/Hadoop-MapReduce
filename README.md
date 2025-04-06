# 🐘 Multi-Node Hadoop Cluster on Ubuntu (HDFS + YARN)

This project demonstrates a complete step-by-step manual setup of a multi-node Hadoop cluster using **Ubuntu Server 20.04**, designed for distributed computing environments. The cluster uses:

- SSH for secure, passwordless communication
- Hadoop 3.2.1 with HDFS and YARN
- Java 8 (OpenJDK)
- Master–Worker architecture
- GUI interfaces for monitoring

---

## 📌 System Architecture

- **h-pc1** → Master Node  
  - NameNode  
  - ResourceManager  

- **h-pc2, h-pc3** → Worker Nodes  
  - DataNodes  
  - NodeManagers  

---

## ⚙️ Step-by-Step Installation

### 🔧 Step 1: Install SSH and PDSH

```bash
sudo apt install ssh
sudo apt install pdsh
