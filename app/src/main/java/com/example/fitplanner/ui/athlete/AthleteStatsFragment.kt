package com.example.fitplanner.ui.athlete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.fitplanner.R
import com.example.fitplanner.data.db.AppDatabase
import com.example.fitplanner.databinding.FragmentAthleteStatsBinding
import com.example.fitplanner.utils.SessionManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.launch
import java.util.*

class AthleteStatsFragment : Fragment() {

    private var _binding: FragmentAthleteStatsBinding? = null
    private val binding get() = _binding!!

    private val db by lazy { AppDatabase.getInstance(requireContext()) }
    private val session by lazy { SessionManager(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAthleteStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val trainings =
                db.trainingPlanDao().getCompletedForAthlete(session.userId)

            showCounters(trainings)
            showWeeklyBarChart(trainings)
        }
    }

    private fun showCounters(trainings: List<com.example.fitplanner.data.db.entities.TrainingPlanEntity>) {
        val now = Calendar.getInstance().timeInMillis

        val weekAgo = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }.timeInMillis
        val monthAgo = Calendar.getInstance().apply { add(Calendar.MONTH, -1) }.timeInMillis
        val yearAgo = Calendar.getInstance().apply { add(Calendar.YEAR, -1) }.timeInMillis

        binding.tvWeek.text =
            "Тренировок за неделю: ${trainings.count { it.trainingDate >= weekAgo }}"
        binding.tvMonth.text =
            "Тренировок за месяц: ${trainings.count { it.trainingDate >= monthAgo }}"
        binding.tvYear.text =
            "Тренировок за год: ${trainings.count { it.trainingDate >= yearAgo }}"
    }

    private fun showWeeklyBarChart(
        trainings: List<com.example.fitplanner.data.db.entities.TrainingPlanEntity>
    ) {
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY

        val weekCounts = IntArray(7)

        trainings.forEach {
            calendar.timeInMillis = it.trainingDate
            val dayIndex = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7
            weekCounts[dayIndex]++
        }

        val entries = weekCounts.mapIndexed { index, count ->
            BarEntry(index.toFloat(), count.toFloat())
        }

        val dataSet = BarDataSet(entries, "").apply {
            color = requireContext().getColor(R.color.purple_500)
            setDrawValues(false)
        }

        val data = BarData(dataSet)
        data.barWidth = 0.55f

        binding.chart.apply {
            this.data = data

            description.isEnabled = false
            legend.isEnabled = false

            setTouchEnabled(false)
            setScaleEnabled(false)
            setPinchZoom(false)

            axisRight.isEnabled = false

            axisLeft.apply {
                axisMinimum = 0f
                granularity = 1f
                setDrawGridLines(false)
                textColor = requireContext().getColor(R.color.black)
            }

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = IndexAxisValueFormatter(
                    listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
                )
                granularity = 1f
                setDrawGridLines(false)
                textColor = requireContext().getColor(R.color.black)
            }

            animateY(1200, Easing.EaseOutBack)
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
