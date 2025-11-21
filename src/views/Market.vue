<template>
  <div class="market-container">

    <!-- 导航栏 -->
    <NavBar />

    <div class="content-layout">
      <!-- 左侧内容：商品列表 -->
      <main class="main-content">
        <!-- 热门装备 -->
        <section class="section">
          <h2 class="section-title">🔥 热门装备</h2>
          <div class="item-grid">
            <ItemCard
              v-for="item in hotItems"
              :key="item.id"
              :item="item"
            />
          </div>
        </section>

        <!-- 24小时涨幅榜 -->
        <section class="section">
          <h2 class="section-title">📈 24小时涨幅榜</h2>
          <div class="item-grid">
            <ItemCard
              v-for="item in topGainers"
              :key="item.id"
              :item="item"
            />
          </div>
        </section>
      </main>

      <!-- 右侧内容：图表 -->
      <aside class="side-content">
        <PriceChart />
      </aside>
    </div>

  </div>
</template>


<script setup>
import { ref, onMounted } from 'vue'
import NavBar from "../components/NavBar.vue"
import ItemCard from "../components/ItemCard.vue"
import PriceChart from "../components/PriceChart.vue"
import { fetchListings } from "../api/market"

const hotItems = ref([])
const topGainers = ref([])

const loadData = async () => {
  try {
    // 获取热门商品 (假设后端支持 sort 参数)
    const hotRes = await fetchListings({ sort: 'hot', limit: 4 })
    hotItems.value = hotRes || []

    // 获取涨幅榜
    const gainersRes = await fetchListings({ sort: 'gainers', limit: 4 })
    topGainers.value = gainersRes || []
  } catch (error) {
    console.error('Failed to load market data:', error)
    // Fallback to mock data if API fails
    hotItems.value = [
      { id: 1, name: "AK47 | 火蛇", price: 45600, change: 3.2, img: "https://via.placeholder.com/300x200" },
      { id: 2, name: "AWP | 二西莫夫", price: 82000, change: -1.5, img: "https://via.placeholder.com/300x200" }
    ]
    topGainers.value = [
      { id: 3, name: "M4A1 | 消音版", price: 28000, change: -1.1, img: "https://via.placeholder.com/300x200" },
      { id: 4, name: "Ak47 | 红线", price: 39000, change: 2.8, img: "https://via.placeholder.com/300x200" }
    ]
  }
}

onMounted(() => {
  loadData()
})
</script>


<style scoped>
.market-container {
  padding: 0;
}

.content-layout {
  display: grid;
  grid-template-columns: 1fr 450px;
  gap: 40px;
  padding: 20px 40px;
  max-width: 100%;
  margin: 0;
}

.side-content {
  position: sticky;
  top: 20px;
  height: fit-content;
}

.section-title {
  font-size: 26px;
  margin: 20px 0;
  color: var(--primary);
}

.item-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 20px;
}

@media (max-width: 1024px) {
  .content-layout {
    grid-template-columns: 1fr;
  }
  .side-content {
    height: 400px;
    position: static;
  }
}
</style>

