package it.unica.foresee.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertArrayEquals;

import it.unica.foresee.datasets.DatasetSparseVector;
import it.unica.foresee.datasets.DoubleElement;
import it.unica.foresee.datasets.MovielensElement;

import it.unica.foresee.datasets.interfaces.ClonableElement;
import it.unica.foresee.libraries.GroupModel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for the GroupModel class.
 */
public class GroupModelTest
{
    @Before
    public void setUp()
    {

    }

    @Test
    public void testAverageStrategy()
    {
        double[] testVector = new double[]{1, 2, 3, 4, 5};
        int listWidth = 10;

        List<List<DatasetSparseVector<DoubleElement>>> clustersList = new ArrayList<>();
        List<DatasetSparseVector<DoubleElement>> clusterOne = new ArrayList<>();

        for (int i = 0; i < listWidth; i++)
        {
            DatasetSparseVector<DoubleElement> el = TestUtils.fillDatasetEntry(testVector);
            el.setId(i + 1);
            clusterOne.add(el);
        }
        clustersList.add(clusterOne);

        GroupModel<DatasetSparseVector<DoubleElement>> model = new GroupModel<>();
        List<DatasetSparseVector<DoubleElement>> modelList = model.averageStrategy(clustersList);

        System.out.println(modelList);

        assertEquals(clusterOne.get(0), modelList.get(0));
    }
}
