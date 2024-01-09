import java.util.ArrayList;

public class CengTreeNodeInternal extends CengTreeNode
{
    private ArrayList<Integer> keys;
    private ArrayList<CengTreeNode> children;

    public CengTreeNodeInternal(CengTreeNode parent)
    {
        super(parent);

        this.type = CengNodeType.Internal;

        this.keys = new ArrayList<>();
        this.children = new ArrayList<>();

    }

    // GUI Methods - Do not modify
    public ArrayList<CengTreeNode> getAllChildren()
    {
        return this.children;
    }

    public Integer keyCount()
    {
        return this.keys.size();
    }

    public Integer keyAtIndex(Integer index)
    {
        if(index >= this.keyCount() || index < 0)
        {
            return -1;
        }
        else
        {
            return this.keys.get(index);
        }
    }

    // Extra Functions

    public Integer childCount() {
        return children.size();
    }

    public CengTreeNode childAtIndex(Integer index) {
        if (index >= childCount() || index < 0) {
            return null;
        }
        return children.get(index);
    }

    public ArrayList<Integer> getKeys() {
    	return this.keys;
    }

    public void addChild(CengTreeNode childNode) {
        if (childNode instanceof CengTreeNodeInternal) {
            CengTreeNodeInternal internalNode = (CengTreeNodeInternal) childNode;
            for (int i = 0; i < children.size(); i++) {
                if (internalNode.keyAtIndex(0) < this.keyAtIndex(i)) {
                    children.add(i, childNode);
                    return;
                }
            }
        }

        else if (childNode instanceof CengTreeNodeLeaf) {
            CengTreeNodeLeaf leafNode = (CengTreeNodeLeaf) childNode;

            for (int i = 0; i < children.size(); i++) {
                if (leafNode.bookKeyAtIndex(0) < this.keyAtIndex(i)) {
                    children.add(i, childNode);
                    return;
                }
            }
        }

        // greatest children
        children.add(childNode);
    }

    public void addKey(Integer key) {
        for (int i = 0; i < keys.size(); i++) {
            if (key < keys.get(i)) {
                keys.add(i, key);
                return;
            }
        }
        // greatest key
        keys.add(key);
    }

    public boolean isExceeded() {
        return keys.size() == (2 * CengTreeNode.order + 1);
    }

    public Integer removeKey() {
        return keys.remove(0);
    }

    public CengTreeNode removeChild() {
        return children.remove(0);
    }

    public String internalNodeToString(String tab) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(tab).append("<index>\n");
        for (Integer key : this.keys) {
            stringBuilder.append(tab)
                .append(key.toString())
                .append('\n');
        }
        stringBuilder.append(tab).append("</index>");

        return stringBuilder.toString();
    }
}
