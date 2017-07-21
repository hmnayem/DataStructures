import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
 
interface HashTreeTraverser
{
    public void addNode(Object node, HashTree subTree);
 
    public void subtractNode();
 
    public void processPath();
}
 
class TreeSearcher implements HashTreeTraverser
{
    Object target;
    HashTree result;
 
    public TreeSearcher(Object t)
    {
        target = t;
    }
 
    public HashTree getResult()
    {
        return result;
    }
 
    public void addNode(Object node, HashTree subTree)
    {
        result = subTree.getTree(target);
        if (result != null)
        {
            throw new RuntimeException("found"); // short circuit traversal
                                                 // when found
        }
    }
 
    @Override
    public void subtractNode()
    {
    }
 
    @Override
    public void processPath()
    {
    }
}
 
class ConvertToString implements HashTreeTraverser
{
    StringBuffer string = new StringBuffer(getClass().getName() + "{");
    StringBuffer spaces = new StringBuffer();
    int depth = 0;
 
    public void addNode(Object key, HashTree subTree)
    {
        depth++;
        string.append("\n" + getSpaces() + key + " {");
    }
 
    public void subtractNode()
    {
        string.append("\n" + getSpaces() + "}");
        depth--;
    }
 
    public void processPath()
    {
    }
 
    public String toString()
    {
        string.append("\n}");
        return string.toString();
    }
 
    private String getSpaces()
    {
        if (spaces.length() < depth * 2)
        {
            while (spaces.length() < depth * 2)
            {
                spaces.append("  ");
            }
        }
        else if (spaces.length() > depth * 2)
        {
            spaces.setLength(depth * 2);
        }
        return spaces.toString();
    }
}
 
@SuppressWarnings({ "rawtypes", "unchecked" })
public class HashTree implements Serializable, Map
{
    private static final long serialVersionUID = 5526070397395889719L;
 
    public HashTree()
    {
        data = new HashMap();
    }
 
    public HashTree(Object key)
    {
        data = new HashMap();
        data.put(key, new HashTree());
    }
 
    public void putAll(Map map)
    {
        if (map instanceof HashTree)
        {
            this.add((HashTree) map);
        }
        else
        {
            throw new UnsupportedOperationException(
                    "can only putAll other HashTree objects");
        }
    }
 
    public Set entrySet()
    {
        return data.entrySet();
    }
 
    public boolean containsValue(Object value)
    {
        return data.containsValue(value);
    }
 
    public Object put(Object key, Object value)
    {
        Object previous = data.get(key);
        add(key, value);
        return previous;
    }
 
    public void clear()
    {
        data.clear();
    }
 
    public Collection values()
    {
        return data.values();
    }
 
    public void add(Object key, HashTree subTree)
    {
        add(key);
        getTree(key).add(subTree);
    }
 
    public void add(HashTree newTree)
    {
        Iterator iter = newTree.list().iterator();
        while (iter.hasNext())
        {
            Object item = iter.next();
            add(item);
            getTree(item).add(newTree.getTree(item));
        }
    }
 
    public HashTree(Collection keys)
    {
        data = new HashMap();
        Iterator it = keys.iterator();
        while (it.hasNext())
        {
            data.put(it.next(), new HashTree());
        }
    }
 
    public HashTree(Object[] keys)
    {
        data = new HashMap();
        for (int x = 0; x < keys.length; x++)
        {
            data.put(keys[x], new HashTree());
        }
    }
 
    public boolean containsKey(Object o)
    {
        return data.containsKey(o);
    }
 
    public boolean isEmpty()
    {
        return data.isEmpty();
    }
 
    public void set(Object key, Object value)
    {
        data.put(key, createNewTree(value));
    }
 
    public void set(Object key, HashTree t)
    {
        data.put(key, t);
    }
 
    public void set(Object key, Object[] values)
    {
        data.put(key, createNewTree(Arrays.asList(values)));
    }
 
    public void set(Object key, Collection values)
    {
        data.put(key, createNewTree(values));
    }
 
    public void set(Object[] treePath, Object[] values)
    {
        if (treePath != null && values != null)
        {
            set(Arrays.asList(treePath), Arrays.asList(values));
        }
    }
 
    public void set(Object[] treePath, Collection values)
    {
        if (treePath != null)
        {
            set(Arrays.asList(treePath), values);
        }
    }
 
    public void set(Collection treePath, Object[] values)
    {
        HashTree tree = addTreePath(treePath);
        tree.set(Arrays.asList(values));
    }
 
    public void set(Collection values)
    {
        clear();
        this.add(values);
    }
 
    public void set(Collection treePath, Collection values)
    {
        HashTree tree = addTreePath(treePath);
        tree.set(values);
    }
 
    public HashTree add(Object key)
    {
        if (!data.containsKey(key))
        {
            HashTree newTree = createNewTree();
            data.put(key, newTree);
            return newTree;
        }
        else
        {
            return getTree(key);
        }
    }
 
    public void add(Object[] keys)
    {
        for (int x = 0; x < keys.length; x++)
        {
            add(keys[x]);
        }
    }
 
    public void add(Collection keys)
    {
        Iterator it = keys.iterator();
        while (it.hasNext())
        {
            add(it.next());
        }
    }
 
    public HashTree add(Object key, Object value)
    {
        add(key);
        return getTree(key).add(value);
    }
 
    public void add(Object key, Object[] values)
    {
        add(key);
        getTree(key).add(values);
    }
 
    public void add(Object key, Collection values)
    {
        add(key);
        getTree(key).add(values);
    }
 
    public void add(Object[] treePath, Object[] values)
    {
        if (treePath != null)
        {
            add(Arrays.asList(treePath), Arrays.asList(values));
        }
    }
 
    public void add(Object[] treePath, Collection values)
    {
        if (treePath != null)
        {
            add(Arrays.asList(treePath), values);
        }
    }
 
    public HashTree add(Object[] treePath, Object value)
    {
        return add(Arrays.asList(treePath), value);
    }
 
    public void add(Collection treePath, Object[] values)
    {
        HashTree tree = addTreePath(treePath);
        tree.add(Arrays.asList(values));
    }
 
    public HashTree add(Collection treePath, Object value)
    {
        HashTree tree = addTreePath(treePath);
        return tree.add(value);
    }
 
    public void add(Collection treePath, Collection values)
    {
        HashTree tree = addTreePath(treePath);
        tree.add(values);
    }
 
    protected HashTree addTreePath(Collection treePath)
    {
        HashTree tree = this;
        Iterator iter = treePath.iterator();
        while (iter.hasNext())
        {
            Object temp = iter.next();
            tree.add(temp);
            tree = tree.getTree(temp);
        }
        return tree;
    }
 
    public HashTree getTree(Object key)
    {
        return (HashTree) data.get(key);
    }
 
    public Object get(Object key)
    {
        return getTree(key);
    }
 
    public HashTree getTree(Object[] treePath)
    {
        if (treePath != null)
        {
            return getTree(Arrays.asList(treePath));
        }
        else
        {
            return this;
        }
    }
 
    public Object clone()
    {
        HashTree newTree = new HashTree();
        cloneTree(newTree);
        return newTree;
    }
 
    protected void cloneTree(HashTree newTree)
    {
        Iterator iter = list().iterator();
        while (iter.hasNext())
        {
            Object key = iter.next();
            newTree.set(key, (HashTree) getTree(key).clone());
        }
    }
 
    protected HashTree createNewTree()
    {
        return new HashTree();
    }
 
    protected HashTree createNewTree(Object key)
    {
        return new HashTree(key);
    }
 
    protected HashTree createNewTree(Collection values)
    {
        return new HashTree(values);
    }
 
    public HashTree getTree(Collection treePath)
    {
        return getTreePath(treePath);
    }
 
    public Collection list()
    {
        return data.keySet();
    }
 
    public Collection list(Object key)
    {
        HashTree temp = (HashTree) data.get(key);
        if (temp != null)
        {
            return temp.list();
        }
        else
        {
            return null;
        }
    }
 
    public Object remove(Object key)
    {
        return data.remove(key);
    }
 
    public Collection list(Object[] treePath)
    {
        if (treePath != null)
        {
            return list(Arrays.asList(treePath));
        }
        else
        {
            return list();
        }
    }
 
    public Collection list(Collection treePath)
    {
        return getTreePath(treePath).list();
    }
 
    public Object replace(Object currentKey, Object newKey)
    {
        HashTree tree = getTree(currentKey);
        data.remove(currentKey);
        data.put(newKey, tree);
        return null;
    }
 
    public Object[] getArray()
    {
        return data.keySet().toArray();
    }
 
    public Object[] getArray(Object key)
    {
        return getTree(key).getArray();
    }
 
    public Object[] getArray(Object[] treePath)
    {
        if (treePath != null)
        {
            return getArray(Arrays.asList(treePath));
        }
        else
        {
            return getArray();
        }
    }
 
    public Object[] getArray(Collection treePath)
    {
        HashTree tree = getTreePath(treePath);
        return tree.getArray();
    }
 
    protected HashTree getTreePath(Collection treePath)
    {
        HashTree tree = this;
        Iterator iter = treePath.iterator();
        while (iter.hasNext())
        {
            Object temp = iter.next();
            tree = tree.getTree(temp);
        }
        return tree;
    }
 
    public int hashCode()
    {
        return data.hashCode() * 7;
    }
 
    public boolean equals(Object o)
    {
        if (!(o instanceof HashTree))
            return false;
        HashTree oo = (HashTree) o;
        if (oo.size() != this.size())
            return false;
        return data.equals(oo.data);
    }
 
    public Set keySet()
    {
        return data.keySet();
    }
 
    public HashTree search(Object key)
    {
        HashTree result = getTree(key);
        if (result != null)
        {
            return result;
        }
        TreeSearcher searcher = new TreeSearcher(key);
        try
        {
            traverse(searcher);
        }
        catch (Exception e)
        {
            // do nothing - means object is found
        }
        return searcher.getResult();
    }
 
    void readObject(ObjectInputStream ois) throws ClassNotFoundException,
            IOException
    {
        ois.defaultReadObject();
    }
 
    void writeObject(ObjectOutputStream oos) throws IOException
    {
        oos.defaultWriteObject();
    }
 
    public int size()
    {
        return data.size();
    }
 
    public void traverse(HashTreeTraverser visitor)
    {
        Iterator iter = list().iterator();
        while (iter.hasNext())
        {
            Object item = iter.next();
            visitor.addNode(item, getTree(item));
            getTree(item).traverseInto(visitor);
        }
    }
 
    private void traverseInto(HashTreeTraverser visitor)
    {
        if (list().size() == 0)
        {
            visitor.processPath();
        }
        else
        {
            Iterator iter = list().iterator();
            while (iter.hasNext())
            {
                Object item = iter.next();
                visitor.addNode(item, getTree(item));
                getTree(item).traverseInto(visitor);
            }
        }
        visitor.subtractNode();
    }
 
    public String toString()
    {
        ConvertToString converter = new ConvertToString();
        traverse(converter);
        return converter.toString();
    }
 
    protected Map data;
 
    public static void main(String args[])
    {
        Collection treePath = Arrays
                .asList(new String[] { "1", "2", "3", "4" });
        HashTree tree = new HashTree();
        tree.add(treePath, "value");
        HashTree tree1 = new HashTree("abcd");
        HashTree tree2 = new HashTree("abcd");
        HashTree tree3 = new HashTree("abcde");
        HashTree tree4 = new HashTree("abcde");
        System.out.println("Is tree1 equals tree2: " + tree1.equals(tree1));
        System.out.println("Is hashcodes of tree1 and tree2 are equal: "
                + (tree1.hashCode() == tree2.hashCode()));
        System.out.println("Is tree3 equals tree3: " + tree3.equals(tree3));
        tree1.add("abcd", tree3);
        System.out.println("Is modified tree1 is equal to tree3: "
                + tree1.equals(tree2));
        tree2.add("abcd", tree4);
        System.out.println("Is modified tree2 equals tree1: "
                + tree1.equals(tree2));
        System.out.println("Is hashcodes are equal: "
                + (tree1.hashCode() == tree2.hashCode()));
    }
}