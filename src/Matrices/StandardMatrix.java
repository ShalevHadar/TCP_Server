package Matrices;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StandardMatrix extends AbstractMatrix implements Serializable {

    /**
     * the StandardMatrix extends the Abstract Class 'AbstractMatrix'
     * we will user the @super method to compute a matrix with the method from the abstract class using our desired
     * params.
     *
     * our main use of this class is to compute a class with random number between 0-1 and use it for our tasks.
     *
     * @param nRows
     * @param nColumns
     */
    public StandardMatrix(int nRows, int nColumns){
        super(nRows,nColumns);
    }
    public StandardMatrix(){
        super(1,1);
    }

    @Override
    public List<Index> neighbors(Index index){
        List<Index> list = new ArrayList<>();
        int extracted;
        try{
            // up
            extracted = intArray[index.getRow()-1][index.getColumn()];
            list.add(new Index(index.getRow()-1,index.getColumn()));
        }catch(IndexOutOfBoundsException outOfBoundsException){}
        try{
            extracted = intArray[index.getRow()+1][index.getColumn()];
            list.add(new Index(index.getRow()+1,index.getColumn()));
        }catch(IndexOutOfBoundsException outOfBoundsException){}
        try{
            extracted = intArray[index.getRow()][index.getColumn()-1];
            list.add(new Index(index.getRow(),index.getColumn()-1));
        }catch(IndexOutOfBoundsException outOfBoundsException){}
        try{
            extracted = intArray[index.getRow()][index.getColumn()+1];
            list.add(new Index(index.getRow(),index.getColumn()+1));
        }catch(IndexOutOfBoundsException outOfBoundsException){}
        return list;
    }


    @Override
    public String demoFunction() {
        return "Type Standard";
    }

    public static void main(String[] args) {

    }
}
