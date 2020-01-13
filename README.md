最近做项目需要展示一个展示广告的跑马灯效果，网上找到一个继承 HorizontalScrollView 实现的，但是不能循环滚动；还有用 RecyclerView 实现的，比较好，但是会可以手动滑动，也不符合需求，下面自己改一下。

#### 实现效果：

- 自动滚动
- 循环滚动
- 不可手动干预（如果想干预也可以）
- 任何View都可以（就是个普通的RecyclerView，内容想是啥自己写就可以了）



#### 实现思路：

**循环滚动：**

和网上一般展示banner的可以循环的轮播图一样，在 RecyclerView.Adapter 中将 getItemCount() 的返回值设为 Integer.MAX_VALUE；然后在 onBindViewHolder 中回调回来的 position 则将会从0一直到 Integer.MAX_VALUE，用 position 和设置的数据长度取余，则会得到真实的 position。

```
    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int realPosition = 0;
        if (mDataList.size() != 0) {
            realPosition = position % mDataList.size();
        }
        holder.update(realPosition);
    }
```

**自动滚动：**

如何让 RecyclerView 自动向后滚动呢，就像手指向后滑动，因为 View 的 scrollBy() 或 scrollTo() 方法移动的是View 的内容，而不是 View 本身，而 RecyclerView 的大小设置为 Integer.MAX_VALUE，刚好可以满足模拟自动滑动，而且 RecyclerView 有复用机制，也不用担心内存问题，所以可以不断的调用 scrollBy() 和 scrollTo() 实现自动滚动。

scrollBy() 和 scrollTo() ：

```
1、向上移动，dx=0，dy>0；向下移动dx=0，dy<0;
2、向左移动，dy=0，dx>0；向右移动dy=0，dx<0;
3、scrollBy()是表示移动了多少，scrollBy(5，0)，会向左移动5，重复调用会一直向左移动；
4、scrollTo()是表示移动到哪里，比如调用scrollTo(5，0)，会向左移动5，重复调用也不会再向左了；
```

根据上面可以一直调用 scrollBy(x,0) 既可向左滚动了，如果用 scrollTo(x,0) 则需要自己维护 x 自增了；

```
	private Handler mHandler = new Handler();
	
	private Runnable mRUnnable = new Runnable() {
        @Override
        public void run() {
        	// 移动的距离，这个可以自己调一下，不过不可以太大，否则看起来不丝滑
            mRv.scrollBy(3, 0);
            // 每隔多少毫秒自动滚动，想控制速度可以改变下移动的距离和时间
            mHandler.postDelayed(this, 10); 
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mRUnnable, 10);
    }
    
        @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRUnnable);
    }

```

**禁止手动滑动：**

如果只是禁调 RecyclerView 的滑动，那就很简单了，LinearLayoutManager 的 canScrollHorizontally() 返回false 就可以了，代码如下：

```
        LinearLayoutManager layoutManager = new LinearLayoutManager(this){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
```

但是这样不只是手动滑动禁止了，自动滚动也禁止了，所以上面方法行不通，可以从 RecyclerView 的触摸事件入手，如下：

```
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent event) {
        return true;
    }
```

这样也有问题，表面上看可以，可以自动滚动了，手动滑动也禁止了，不过连点击事件也拦截了，不过到这里已经很好办了，下面处理一下：

```
    private float downX, downY;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getX() - downX) < 5 && Math.abs(event.getY() - downY) < 5) {
                    return false;
                } else {
                    return true;
                }
            case MotionEvent.ACTION_UP:
                View childView = rv.findChildViewUnder(event.getX(), event.getY());
                int position = -1;
                if (childView != null)
                    position = rv.getChildLayoutPosition(childView);
                    
                Log.e("Bill", "click item:" + position);
                break;
        }

        return false;
    }
```

在手指按下时记录按下坐标，这时不能拦截，否则后面不会走了，点击事件也屏蔽掉了，然后在move的时候判断一下，移动的坐标跟按下的坐标一样，不拦截，则可以相应手指up事件，RecyclerView 的点击事件自然也不会被拦截；如果移动的坐标跟按下的坐标不一样，则说明滑动了，这时在move时拦截掉，避免 RecyclerView 被滑动；我这里加了个误差判断，即5以内算点击了；另外在因为有RecyclerView和手指的x、y，所以可以计算点击的是哪个position了，这个可以模仿ListView实现item的点击事件了，不用在Adapter中单独实现了；当然了，在Adapter中实现也可以。

## [项目地址](https://github.com/YBill/MarqueeView)



参考链接：<http://www.word666.com/ruanjian/143001.html>
