package gameoflife.helper;

import org.junit.Test;

public class SeedHelperTest {
    private final SeedHelper seedHelper = new SeedHelper();

    @Test(expected = RuntimeException.class)
    public void testSeedToMapWithNull() {
        seedHelper.seedToMap((String)null);
    }

    @Test(expected = RuntimeException.class)
    public void testSetBoundaryWithNull() {
        seedHelper.seedToMap(new String[0]);
    }

}
