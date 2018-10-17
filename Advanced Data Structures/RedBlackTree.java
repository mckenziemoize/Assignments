/**
 * The object class for Red Black Tree nodes
 */
class Node {
    static final char BLACK = 'B', RED = 'R'; // The two color choices
    
    Node right, left; // The children
    int data; char color; // The properties
    
    public Node(int data) {
        this.data = data;
        color = RED; // All new nodes are red
        right = left = null; // Set both children to null
    }
    
    @Override
    public String toString() {
        return "(" + data + "" + color + ")";
    }
}

/**
 * The object class for Red Black Trees
 */
class RedBlackTree {
    Node root;
    
    // Empty tree creation
    public RedBlackTree() {
        root = null;
    }
    
    // Add nodes at initialization
    public RedBlackTree(int... datas) {
        this();
        add(datas);
    }
    
    /**
     * Add as many Nodes to the tree as you want
     * 
     * @param datas The list of Nodes being added to the tree
     */
    public void add(int... datas) {
        for (int data : datas) {
            root = add(root, data); // Add each new node individually
            root.color = Node.BLACK; // Ensure the root is always black
        }
    }
    
    // Recursively add Nodes to the tree
    private Node add(Node current, int data) {
        if (current == null) {
            return new Node(data);
        }
        
        // Bring blackness if you find red twins
        if (hasTwoRedChildren(current)) {
            bringBlackness(current);
        }

        // Insert as a BST
        if (data < current.data) {
            current.left = (add(current.left, data));
        } else {
            current.right = (add(current.right, data));
        }
        
        // Rotations
        current = rotate(current);
        
        return current;
    }
    
    // Method to determine if a node is red accounting for null nodes
    private boolean isRed(Node current) {
        if (current == null) {
            return false; // null nodes are not red
        }
        return current.color == Node.RED;
    }
    
    // Brings the blackness down from parent
    private void bringBlackness(Node parent) {
        // Parent becomes red
        parent.color = Node.RED;
        
        // Children become black
        parent.right.color = Node.BLACK; 
        parent.left.color = Node.BLACK;
    }
    
    // Determines if you need to bring the blackness down
    private boolean hasTwoRedChildren(Node current) {
        // Check for null, then check for reds
        return current.right != null && current.left != null && isRed(current.right) && isRed(current.left);
    }
    
    // Rotation Determiner Method
    private Node rotate(Node current) {
        if (isRed(current.left)){
            if (isRed(current.left.left)) { // Single Right Rotation
                current = rightRotation(current);
            } else if (isRed(current.left.right)) { // Left Right Rotation
                current.left = leftRotation(current.left);
                return rightRotation(current);
            }
        } else if (isRed(current.right)) {
            if (isRed(current.right.right)) { // Single Left Rotation
                return leftRotation(current);
            } else if (isRed(current.right.left)) { // Right Left Rotation
                current.right = rightRotation(current.right);
                return leftRotation(current);
            }
        }
        
        return current;
    }
    
    // Right Rotation
    private Node rightRotation(Node grand) {
        // Assigning
        Node parent = grand.left;
        Node rightChild = parent.right;
        
        // Movement
        parent.right = grand;
        grand.left = rightChild;
        
        // Coloring
        parent.color = Node.BLACK;
        parent.right.color = Node.RED;
        
        return parent;
    }
    
    // Left Rotation
    private Node leftRotation(Node grand) {
        // Assigning
        Node parent = grand.right;
        Node leftChild = parent.left;
        
        // Movement
        parent.left = grand;
        grand.right = leftChild;
        
        // Coloring
        parent.color = Node.BLACK;
        parent.left.color = Node.RED;
        
        return parent;
    }
    
    /**
     * Displays the inorder traversal of the tree 
     * (Left, Root, Right)
     */
    public void inorder() {
        System.out.println("Inorder: " + inorder(root) + "\b\b");
    }
    
    // Determine the inorder recursively
    private String inorder(Node current) {
        String output = "";
        
        if (current != null) {
            output += inorder(current.left);
            output += current + ", ";
            output += inorder(current.right);
        }
        
        return output;
    }
    
    /**
     * Displays the preorder traversal of the tree
     * (Root, Left, Right)
     */
    public void preorder() {
        System.out.println("Preorder: " + preorder(root) + "\b\b");
    }
    
    // Determine the preorder recursively
    private String preorder(Node current) {
        String output = "";
        
        if (current != null) {
            output += current + ", ";
            output += preorder(current.left);
            output += preorder(current.right);
        }
        
        return output;
    }

    /**
    * Public test method for displaying the tree
    */
    public static void main(String[] args) {
        RedBlackTree rbt = new RedBlackTree(19,20,25,12,17,23,24); // Add the required nodes
        
        rbt.inorder(); // Print the inorder traversal
        rbt.preorder(); // Print the preorder traversal
        
        colorTable(rbt); // Print the table showing nodes and colors
    }
    
    /**
     * Prints the required color table for the Red Black Tree following 
     * insertion order. (We are allowed to hard code this portion of the code)
     * 
     * @param rbt The test tree
     */
    public static void colorTable(RedBlackTree rbt) {
        System.out.printf("\n%-5s | %5s\n", "Node", "Color"); // Header for the table
        System.out.println("-------------");
        
        Node current = rbt.root.left.right; // (19)
        System.out.printf("%-5s | %5s\n", current.data, current.color);
        
        current = rbt.root; // (20)
        System.out.printf("%-5s | %5s\n", current.data, current.color);
        
        current = rbt.root.right.right; // (25)
        System.out.printf("%-5s | %5s\n", current.data, current.color);
        
        current = rbt.root.left.left; // (12)
        System.out.printf("%-5s | %5s\n", current.data, current.color);
        
        current = rbt.root.left; // (17)
        System.out.printf("%-5s | %5s\n", current.data, current.color);
        
        current = rbt.root.right.left; // (23)
        System.out.printf("%-5s | %5s\n", current.data, current.color);
        
        current = rbt.root.right; // (24)
        System.out.printf("%-5s | %5s\n", current.data, current.color);
    }
}