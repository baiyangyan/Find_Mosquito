package hlh.findmosquito

import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.recycler_itme.view.*

/**
 * 作者： Created by Ying on 2018/7/23.
 * 描述：
 */
class RecyclerAdapter(val items: java.util.ArrayList<Int>, width: Int) : RecyclerView.Adapter<RecyclerAdapter.RcyclerHolder>() {
    var b = false;  //true 为使用小旗子
    var bb = false //是否继续点击
    var inumber = 9
    var width = width
    val mine = ArrayList<Int>()
    override fun onBindViewHolder(holder: RcyclerHolder?, position: Int) {
        val view = holder?.bind(items, position, mine, width)
        view!!.setOnClickListener {
            if (bb) return@setOnClickListener
            if (b) {
                items.set(position, if (items[position] == 0) 1 else 0)
            } else if (items[position] == 0) {
                when (mine[position]) {
                    0 -> { //空白
                        looking(items, position, mine)
                    }
                    -1 -> {  //蚊子
                        items[position] = 5
                    }
                    else -> { //数字
                        items[position] = 4
                    }
                }
            }
            notifyDataSetChanged()
            var inumer = 0
            for (i in items) {
                if (i == 0 || i == 1) inumer++
            }
            // if (!items.contains(0)) {
            if (inumer == inumber || items.contains(5)) {
                bb = true
                val builder = AlertDialog.Builder(view.context)
                val textView = TextView(view.context)
                textView.text = if (items.contains(5)) "失败咯 o‿≖✧" else "这都能过啊 (●'◡'●)ﾉ♥   厉害了 "
                textView.textSize = 35F
                builder.setView(textView)
                val onClickListener = object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        dialog.dismiss()
                    }
                }
                builder.setPositiveButton("确定", onClickListener).create().show()
            }
        }
    }

    override fun getItemCount(): Int = items.size;

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerAdapter.RcyclerHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.recycler_itme, parent, false)

        return RcyclerHolder(view)
    }

    fun addAll(arr: ArrayList<Int>) {
        mine.clear()
        mine.addAll(arr)
        notifyDataSetChanged()
    }

    fun looking(items: ArrayList<Int>, position: Int, mine: ArrayList<Int>) {
        items[position] = 2
        val intArrayOf: IntArray
        if (position % 9 == 0) intArrayOf = intArrayOf(position - 9, position - 8, position + 1, position + 9, position + 10)
        else if (position % 9 == 8) intArrayOf = intArrayOf(position - 10, position - 9, position - 1, position + 8, position + 9)
        else intArrayOf = intArrayOf(position - 10, position - 9, position - 8, position - 1, position + 1, position + 8, position + 9, position + 10)
        for (i in intArrayOf) {
            if (i < mine.size && i >= 0 && items[i] == 0)
                when (mine[i]) {
                    0 -> { //空白
                        looking(items, i, mine)
                    }
                    else -> { //数字
                        items[i] = 4
                    }
                }
        }
    }

    class RcyclerHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(items: ArrayList<Int>, position: Int, mine: ArrayList<Int>, width: Int): View {
            var name = items[position]
            val recyclerItme_imageView = view.recyclerItme_imageView
            val layoutParams = view.getLayoutParams();
            layoutParams.width = width / 9;
            layoutParams.height = width / 9;
            view.setLayoutParams(layoutParams);

            if (items.contains(5)) {
                view.recyclerItme_textView.visibility = View.VISIBLE
                view.recyclerItme_imageView.visibility = View.GONE
                view.recyclerItme_textView.text = if (name == 2) "" else mine[position].toString()
                if (mine[position] == -1) {
                    view.recyclerItme_imageView.visibility = View.VISIBLE
                    recyclerItme_imageView.setImageResource(R.mipmap.a)
                }
            } else
                when (name) {
                    0 -> { //可以点击
                        view.recyclerItme_textView.visibility = View.GONE
                        view.recyclerItme_imageView.visibility = View.GONE
                        // view.setBackgroundColor(view.context.getResources().getColor(R.color.colorback))
                    }
                    3, 1 -> { //蚊子   //红旗
                        view.recyclerItme_textView.visibility = View.GONE
                        view.recyclerItme_imageView.visibility = View.VISIBLE
                        recyclerItme_imageView.setImageResource(if (name == 1) R.mipmap.qz else R.mipmap.ic_launcher)
                    }
                    4, 2 -> {//数字 //空白
                        view.recyclerItme_textView.visibility = View.VISIBLE
                        view.recyclerItme_textView.text = if (name == 2) "" else mine[position].toString()
                        view.recyclerItme_imageView.visibility = View.GONE
                    }
                }
            return view
        }
    }
}

