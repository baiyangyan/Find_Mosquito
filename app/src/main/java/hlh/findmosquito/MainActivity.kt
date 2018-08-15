package hlh.findmosquito

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    val items = ArrayList<Int>(); //状态 可点击状态0  不可点击状态(标记为红旗1 空白2,蚊子3,数字4)
    val mine = ArrayList<Int>();  //雷位置  雷 -1
    var adapter: RecyclerAdapter? = null
    val rand = Random()
    var inumber = 9  //随机雷数
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mineNumber(inumber)
        recyclerView.layoutManager = GridLayoutManager(this, 9)
        adapter = RecyclerAdapter(items, windowManager.defaultDisplay.width)
        recyclerView.adapter = adapter;
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.itme_divider_v))
        recyclerView.addItemDecoration(dividerItemDecoration)
        val dividerItemDecoration1 = DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL)
        dividerItemDecoration1.setDrawable(ContextCompat.getDrawable(this, R.drawable.itme_divider_h))
        recyclerView.addItemDecoration(dividerItemDecoration1)
        adapter!!.mine.addAll(mine)
    }

    override fun onResume() {
        super.onResume()
        dialog = AlertDialog.Builder(this).setTitle("请选择关卡").setView(R.layout.dialog_view).create()
    }

    fun mineNumber(row: Int) {
        items.clear()
        mine.clear()
        val ints = arrayOfNulls<Int>(9 * 9)
        val number = 9 * 9

        var i0 = 0
        while (i0 < row) {
            val nextInt = rand.nextInt(number)
            if (!ints.contains(nextInt)) {
                ints[i0] = nextInt
                i0++
            }
        }

        for (i in 0..number - 1) {
            items.add(0)

            val intss: IntArray
            if (i % 9 == 0) intss = intArrayOf(i - 9, i - 8, i, i + 1, i + 9, i + 10)
            else if (i % 9 == 8) intss = intArrayOf(i - 10, i - 9, i - 1, i, i + 8, i + 9)
            else intss = intArrayOf(i - 10, i - 9, i - 8, i - 1, i, i + 1, i + 8, i + 9, i + 10)

            if (ints.contains(i))
                mine.add(-1)
            else {
                var jnumber = 0
                for (j in 0..intss.size - 1) {
                    if (ints.contains(intss[j])) ++jnumber
                }
                mine.add(jnumber)
            }
        }
    }

    var dialog: AlertDialog? = null
    //关卡
    fun Refresh1(view: View) {
        dialog!!.show()
    }

    fun button(view: View) {
        when (view.id) {
            R.id.button -> {
                inumber = 9
            }
            R.id.button2 -> {
                inumber = 2 * 9
            }
            R.id.button3 -> {
                inumber = 3 * 9
            }
            R.id.button4 -> {
                inumber = 4 * 9
            }
            R.id.button5 -> {
                inumber = 5 * 9
            }
        }
        adapter?.inumber = inumber
        level.text = (view as Button).text.toString()
        dialog!!.dismiss()
        Refresh(view)
    }

    //刷新
    fun Refresh(view: View) {
        mineNumber(inumber)
        adapter?.bb = false
        adapter!!.addAll(mine)

    }

    //旗子
    fun QiZi(view: View) {
        adapter!!.b = !adapter!!.b
        view.setBackgroundColor(getResources().getColor
        (if (adapter!!.b) R.color.colorButtonback1 else R.color.colorButtonback))
    }
}
