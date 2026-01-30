# 💾 Memory Management Simulator for Operating Systems

Memory management is a fundamental component of modern operating systems, responsible for efficiently handling how memory is allocated, accessed, and managed by applications and processes. It ensures that critical data and instructions are readily available to the system when needed, maximizing both performance and resource utilization.

This project simulates two key types of memory:

- **Main Memory (RAM):** A volatile storage used to temporarily hold running processes, instructions, and data. It requires power to retain data and is directly accessible by the CPU.
- **Virtual Memory:** A non-volatile form of memory created by the OS when RAM is insufficient. It uses disk space to simulate extra memory and enables multitasking, memory protection, and efficient resource use.

This simulator helps visualize how these two memory types are managed, and how OS decisions affect system performance.

## 🛠️ Features

### 📍 Main Memory Management

> Simulates the management of a contiguous block of memory with user-defined size, including:
>
> - Allocation and deallocation of memory blocks  
> - Memory compaction to reduce fragmentation  
> - Visualizing the status of allocated and free regions
>
> #### 🧩 Memory Allocation (RQ)
>
> Supports three memory allocation strategies:
>
> | Strategy   | Flag | Description                                  |
> |------------|------|----------------------------------------------|
> | First Fit  | `F`  | Allocates the first suitable free block      |
> | Best Fit   | `B`  | Allocates the smallest block that fits       |
> | Worst Fit  | `W`  | Allocates the largest available block        |
>
> #### 🧹 Memory Release (RL)
>
> Deallocates a memory block. If the released block is adjacent to a free region, it merges them into a single hole to reduce fragmentation and improve allocation efficiency.
>
> #### 📦 Memory Compaction (C)
>
> Shifts allocated memory blocks to combine all free spaces into one large block, maximizing space utilization and reducing fragmentation.
>
> #### 📊 Status Report (STAT)
>
> Displays the current state of memory:
> - Allocated regions  
> - Free regions  
> - Memory usage summary

### 📍 Virtual Memory Simulation

> Simulates the translation of logical addresses to physical addresses for a 65,536-byte virtual address space.
>
> #### 🧭 Logical to Physical Address Translation
> - Reads 100 logical addresses from a file (`addresses.txt`)
> - Translates each address using a page table
> - Stores the correct signed byte into physical memory
>
> #### 🧪 Testing and Validation
> - Randomly selects 5 logical addresses  
> - Outputs:
>   - Logical Address  
>   - Physical Address  
>   - Corresponding signed byte value from physical memory  
> - Compares values with expected results
>
> #### 📈 Statistics
> Prints:
> - Total number of addresses processed  
> - Number of page faults  
> - Hit/miss rate for address translation
>
> #### 🔁 Page Replacement (FIFO)
> Implements a **First-In-First-Out (FIFO)** page replacement policy:
> - Handles page faults when memory is full  
> - Replaces the oldest loaded page with the new one  
> - Updates both the page table and physical memory  
> - Outputs details of each page replacement

## 📁 Project Structure

   ```bash
   memory_management_os/
   ├── src/
   │   ├── part1/                             
   │   │   ├── block.java                    # Class for memory block structure
   │   │   └── mainMemorySimulator.java      # Main memory management simulator
   │   └── part2/
   │       └── virtualMemorySimulator.java   # Virtual memory management simulator
   ├── addresses.txt
   ├── correct.txt
   ├── execution-output-for-all-test-cases.pdf
   └── README.md
   ```
## 👨‍💻 Authors
- Reem Saleh Saeed Al Malki
- Asail Mashhour Al Amoudi
- Shahad Maher Magram
- Seham Khaldoun Nahlawi
