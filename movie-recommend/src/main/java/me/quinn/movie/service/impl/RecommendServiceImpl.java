package me.quinn.movie.service.impl;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
@Service
public class RecommendServiceImpl {
    @Autowired
    DataSource dataSource;

    /**
     * 基于内容的推荐算法
     * @param id 用户id
     * @param num
     * @return
     * @throws TasteException
     */
    public List<RecommendedItem> recommenderItem(Long id, Integer num) throws TasteException {

        //构建数据模型
        JDBCDataModel model = new MySQLJDBCDataModel(dataSource, "rate",
                "user_id", "movie_id", "rate","null");

        ReloadFromJDBCDataModel dataModel = new ReloadFromJDBCDataModel(model);
        //计算内容相似度
        ItemSimilarity similarity = new EuclideanDistanceSimilarity(dataModel);
        //构建推荐引擎
        Recommender r = new GenericItemBasedRecommender(dataModel, similarity);
        //返回推荐结果
        return r.recommend(id, num);

    }

    public List<RecommendedItem> recommenderUser(Long id,Integer num) throws TasteException{
        //构建数据模型
        JDBCDataModel model = new MySQLJDBCDataModel(dataSource, "rate",
                "user_id", "movie_id", "rate","null");

        ReloadFromJDBCDataModel dataModel = new ReloadFromJDBCDataModel(model);
        //计算内容相似度
        UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);//用PearsonCorrelation 算法计算用户相似度
        //计算邻居
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, similarity, dataModel);//计算用户的“邻居”，这里将与该用户最近距离为 3 的用户设置为该用户的“邻居”。
        //构建推荐引擎
        Recommender r = new GenericUserBasedRecommender(dataModel,neighborhood,similarity);
        //返回推荐结果
        return r.recommend(id, num);
    }
}
