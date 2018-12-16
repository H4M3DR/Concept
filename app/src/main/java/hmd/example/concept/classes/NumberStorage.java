package hmd.example.concept.classes;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import hmd.example.concept.models.SimpleNumber;

public class NumberStorage {
    private MutableLiveData<List<SimpleNumber>> initialNumbers = new MutableLiveData<>();
    private MutableLiveData<List<SimpleNumber>> laterNumbers = new MutableLiveData<>();
    private int numItems;
    private int maxItems;
    //indicating that is already getting more numbers
    private boolean isLoadingMoreNumbers = false;

    public NumberStorage(int initialNumber,int maxItems) {
        this.numItems += initialNumber;
        this.maxItems = maxItems;
        //adding some numbers async-ly
        new Thread(() -> {
            List<SimpleNumber> nums = new ArrayList<>();
            for (int i = 0; i < numItems ; i++) {
                nums.add(new SimpleNumber());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            initialNumbers.postValue(nums);
        }).start();
    }

    //this method fetch more numbers async-ly
    public void getMoreNumbers()
    {
        boolean isLoading ;
        synchronized (this){
            isLoading = isLoadingMoreNumbers;
        }
        if (numItems >= maxItems || isLoading)
        {
            return;
        }
        synchronized (this) {
            isLoadingMoreNumbers = true;
        }

        new Thread(() -> {
            int numberOfItems = Constants.recyclerViewPagingItemsCount;
            this.numItems += numberOfItems;
            List<SimpleNumber> nums = new ArrayList<>();
            for (int i = 0; i < numberOfItems ; i++) {
                nums.add(new SimpleNumber());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            laterNumbers.postValue(nums);
            synchronized (NumberStorage.this) {
                isLoadingMoreNumbers = false;
            }
        }).start();
    }

    //getter for initial numbers
    public MutableLiveData<List<SimpleNumber>> getInitialNumbers() {
        return initialNumbers;
    }
    //getter for later numbers
    public MutableLiveData<List<SimpleNumber>> getLaterNumbers() {
        return laterNumbers;
    }

}
