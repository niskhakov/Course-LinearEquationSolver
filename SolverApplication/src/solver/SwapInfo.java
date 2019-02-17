package solver;

public class SwapInfo {
    private int prevIndex;
    private int nextIndex;

    SwapInfo(int prevIndex, int nextIndex) {
        this.prevIndex = prevIndex;
        this.nextIndex = nextIndex;
    }

    public int[] getSwapInfo() {
        return new int[]{prevIndex, nextIndex};
    }

    public int[] getReversedSwapInfo() {
        return new int[]{nextIndex, prevIndex};
    }
}
