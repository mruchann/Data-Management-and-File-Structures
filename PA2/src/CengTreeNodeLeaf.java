import java.util.ArrayList;

public class CengTreeNodeLeaf extends CengTreeNode
{
    private ArrayList<CengBook> books;
    private CengTreeNodeLeaf leftLeafNode;
    private CengTreeNodeLeaf rightLeafNode;

    public CengTreeNodeLeaf(CengTreeNode parent)
    {
        super(parent);

        this.type = CengNodeType.Leaf;

        this.books = new ArrayList<>();
    }

    // GUI Methods - Do not modify
    public int bookCount()
    {
        return books.size();
    }
    public Integer bookKeyAtIndex(Integer index)
    {
        if(index >= this.bookCount()) {
            return -1;
        } else {
            CengBook book = this.books.get(index);

            return book.getBookID();
        }
    }

    // Extra Functions
    public ArrayList<CengBook> getBooks() {
    	return this.books;
    }

    public void add(CengBook book) {
        for (int i = 0; i < books.size(); i++) {
            if (book.getBookID() <= books.get(i).getBookID()) {
                books.add(i, book);
                return;
            }
        }
        books.add(book);
    }

    public CengBook remove() {
        return books.remove(0);
    }

    public CengTreeNodeLeaf getLeftLeafNode() {
        return leftLeafNode;
    }

    public void setLeftLeafNode(CengTreeNodeLeaf leftLeafNode) {
        this.leftLeafNode = leftLeafNode;
    }

    public CengTreeNodeLeaf getRightLeafNode() {
        return rightLeafNode;
    }

    public void setRightLeafNode(CengTreeNodeLeaf rightLeafNode) {
        this.rightLeafNode = rightLeafNode;
    }

    public String leafNodeToString(String tab) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(tab).append("<data>\n");
        for (CengBook cengBook : this.books) {
            stringBuilder.append(tab)
                .append(this.leafNodeRecordToString(cengBook))
                .append('\n');
        }
        stringBuilder.append(tab).append("</data>");

        return stringBuilder.toString();
    }


    // <record>34|Why Fish Don’t Exist|Lulu Miller|Non-fiction</record>
    public String leafNodeRecordToString(CengBook cengBook) {

        return "<record>" + cengBook.fullName() + "</record>";
    }
}
