import java.util.ArrayList;
import java.util.NoSuchElementException;

public class CengTree
{
    public CengTreeNode root;

    private final Integer capacity;

    public CengTree(Integer order) {
        CengTreeNode.order = order;
        this.capacity = 2 * order;
        this.root = new CengTreeNodeLeaf(null);
    }

    public void addBook(CengBook book) {
        this.addBookRecursive(this.root, book);
    }

    public void printTree() {
        this.printTreeRecursive(this.root, 0);
    }

    public ArrayList<CengTreeNode> searchBook(Integer bookID) {

        ArrayList<CengTreeNode> visitedBooks = new ArrayList<>();

        try {
            searchBookRecursive(this.root, bookID, visitedBooks);
        }
        catch (NoSuchElementException e) {
            System.out.printf("Could not find %d.%n", bookID);
            return null;
        }

        StringBuilder tab = new StringBuilder();

        for (CengTreeNode cengTreeNode : visitedBooks) {
            if (cengTreeNode instanceof CengTreeNodeInternal) {
                CengTreeNodeInternal internalNode = (CengTreeNodeInternal) cengTreeNode;

                System.out.println(internalNode.internalNodeToString(tab.toString()));
            }
            else if (cengTreeNode instanceof CengTreeNodeLeaf) {
                CengTreeNodeLeaf leafNode = (CengTreeNodeLeaf) cengTreeNode;
                for (CengBook cengBook : leafNode.getBooks()) {
                    if (cengBook.getBookID().equals(bookID)) {
                        System.out.println(tab + leafNode.leafNodeRecordToString(cengBook));
                        return visitedBooks;
                    }
                }
            }
            tab.append('\t');
        }

        return visitedBooks;
    }

    private void addBookRecursive(CengTreeNode root, CengBook book) {

        if (root.type.equals(CengNodeType.Internal)) {
            CengTreeNodeInternal internalNode = (CengTreeNodeInternal) root;

            for (int i = 0; i < internalNode.keyCount(); i++) {
                if (book.getBookID() < internalNode.keyAtIndex(i)) {
                    this.addBookRecursive(internalNode.childAtIndex(i), book);
                    return;
                }
            }
            // greatest
            this.addBookRecursive(internalNode.childAtIndex(internalNode.keyCount()), book);
        }

        else if (root.type.equals(CengNodeType.Leaf)) {
            CengTreeNodeLeaf leafNode = (CengTreeNodeLeaf) root;
            leafNode.add(book);

            if (leafNode.bookCount() <= capacity) {
                return;
            }

            // split leaf
            boolean rootIsLeaf = (leafNode.getParent() == null);
            if (rootIsLeaf) {
                CengTreeNodeInternal newRootNode = new CengTreeNodeInternal(null);
                leafNode.setParent(newRootNode);
                this.root = newRootNode;
            }
            this.splitLeaf(leafNode, rootIsLeaf);

            CengTreeNodeInternal parentNode = (CengTreeNodeInternal) leafNode.getParent();

            if (parentNode.isExceeded()) {
                this.splitInternal(parentNode);
            }
        }
    }

    private void printTreeRecursive(CengTreeNode root, int level) {
        if (root.type.equals(CengNodeType.Internal)) {
            CengTreeNodeInternal internalNode = (CengTreeNodeInternal) root;

            System.out.println(internalNode.internalNodeToString(this.levelToTab(level)));

            for (int i = 0; i < internalNode.childCount(); i++) {
                this.printTreeRecursive(internalNode.childAtIndex(i), level+1);
            }
        }

        else if (root.type.equals(CengNodeType.Leaf)) {
            CengTreeNodeLeaf leafNode = (CengTreeNodeLeaf) root;

            System.out.println(leafNode.leafNodeToString(this.levelToTab(level)));
        }
    }

    private void searchBookRecursive(CengTreeNode root, Integer bookID, ArrayList<CengTreeNode> visitedBooks) {

        if (root.type.equals(CengNodeType.Internal)) {
            CengTreeNodeInternal internalNode = (CengTreeNodeInternal) root;

            visitedBooks.add(internalNode);

            for (int i = 0; i < internalNode.keyCount(); i++) {
                if (bookID < internalNode.keyAtIndex(i)) {
                    this.searchBookRecursive(internalNode.childAtIndex(i), bookID, visitedBooks);
                    return;
                }
            }
            // greatest
            this.searchBookRecursive(internalNode.childAtIndex(internalNode.keyCount()), bookID, visitedBooks);
        }

        else if (root.type.equals(CengNodeType.Leaf)) {
            CengTreeNodeLeaf leafNode = (CengTreeNodeLeaf) root;

            visitedBooks.add(leafNode);

            for (CengBook cengBook : leafNode.getBooks()) {
                if (cengBook.getBookID().equals(bookID)) {
                    return;
                }
            }

            throw new NoSuchElementException();
        }
    }

    //      newRootNode
    // newLeafNode, leafNode
    //    Left       Right

    //                  parentNode
    // newLeafNode, leafNode, anotherLeafNode
    //    Left       Right

    private void splitLeaf(CengTreeNodeLeaf leafNode, boolean rootWasLeaf) {

        CengTreeNodeInternal parentNode = (CengTreeNodeInternal) leafNode.getParent();
        CengTreeNodeLeaf newLeafNode = new CengTreeNodeLeaf(parentNode);


        for (int i = 0; i < capacity / 2; i++) {
            newLeafNode.add(leafNode.remove());
        }

        // copy up
        parentNode.addKey(leafNode.bookKeyAtIndex(0));

        parentNode.addChild(newLeafNode);
        if (rootWasLeaf) {
            parentNode.addChild(leafNode);
        }

        // Doubly linked list
        CengTreeNodeLeaf leftNode = leafNode.getLeftLeafNode();
        leafNode.setLeftLeafNode(newLeafNode);
        newLeafNode.setRightLeafNode(leafNode);
        newLeafNode.setLeftLeafNode(leftNode);
        if (leftNode != null) {
            leftNode.setRightLeafNode(newLeafNode);
        }
    }

    private void splitInternal(CengTreeNodeInternal parentNode) {

        CengTreeNodeInternal parentParentNode = (CengTreeNodeInternal) parentNode.getParent();

        if (parentParentNode == null) {
            CengTreeNodeInternal newRootNode = new CengTreeNodeInternal(null);
            parentNode.setParent(newRootNode);
            this.root = newRootNode;

            CengTreeNodeInternal newInternalNode = new CengTreeNodeInternal(parentNode.getParent());

            for (int i = 0; i < capacity / 2; i++) {
                newInternalNode.addKey(parentNode.removeKey());
                newInternalNode.addChild(parentNode.removeChild());
            }
            newInternalNode.addChild(parentNode.removeChild());

            // setting the parent of new migrated children
            for (CengTreeNode child : newInternalNode.getAllChildren()) {
                child.setParent(newInternalNode);
            }

            // move up
            newRootNode.addKey(parentNode.removeKey());

            newRootNode.addChild(newInternalNode);
            newRootNode.addChild(parentNode);
        }
        else {
            CengTreeNodeInternal newInternalNode = new CengTreeNodeInternal(parentNode.getParent());

            for (int i = 0; i < capacity / 2; i++) {
                newInternalNode.addKey(parentNode.removeKey());
                newInternalNode.addChild(parentNode.removeChild());
            }
            newInternalNode.addChild(parentNode.removeChild());

            // setting the parent of new migrated children
            for (CengTreeNode child : newInternalNode.getAllChildren()) {
                child.setParent(newInternalNode);
            }

            // move up
            parentParentNode.addKey(parentNode.removeKey());

            parentParentNode.addChild(newInternalNode);

            if (parentParentNode.isExceeded()) {
                this.splitInternal(parentParentNode);
            }
        }
    }

    private String levelToTab(int level) {
        StringBuilder tab = new StringBuilder();
        while (level --> 0) {
            tab.append('\t');
        }
        return tab.toString();
    }
}
