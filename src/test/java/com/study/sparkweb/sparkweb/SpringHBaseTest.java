package com.study.sparkweb.sparkweb;

import com.study.sparkweb.sparkweb.domain.People;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class SpringHBaseTest {

    @Autowired
    private HbaseTemplate hbaseTemplate;


    //测试创建名字空间和表
    @Test
    public void testCreateTable() throws IOException {
        Connection conn = ConnectionFactory.createConnection(hbaseTemplate.getConfiguration());
        Admin admin = conn.getAdmin();
        NamespaceDescriptor nss12 = NamespaceDescriptor.create("ns12").build();
        admin.createNamespace(nss12);
        TableName tableName = TableName.valueOf("ns12:t1");
        HTableDescriptor table = new HTableDescriptor(tableName);
        HColumnDescriptor f1 = new HColumnDescriptor("f1");
        table.addFamily(f1);
        byte[][] splitKey = new byte[][]{Bytes.toBytes("r10000"),Bytes.toBytes("r20000")};
        admin.createTable(table, splitKey);

    }

    //测试插入数据
    @Test
    public void testPutData() {
        hbaseTemplate.put("ns12:t1","r00001","f1","id",Bytes.toBytes(1));
        hbaseTemplate.put("ns12:t1","r00001","f1","name",Bytes.toBytes("tom"));
        hbaseTemplate.put("ns12:t1","r00001","f1","age",Bytes.toBytes(20));
        hbaseTemplate.put("ns12:t1","r00001","f1","sex",Bytes.toBytes("man"));
    }

    //使用回调程序插入数据或做任何事
    @Test
    public void testCallBack() {
        hbaseTemplate.execute("ns12:t1", new TableCallback<Object>() {
            @Override
            public Object doInTable(HTableInterface table) throws Throwable {
                Put put = new Put(Bytes.toBytes("r00002"));
                put.addColumn(Bytes.toBytes("f1"),Bytes.toBytes("id"),Bytes.toBytes(1));
                put.addColumn(Bytes.toBytes("f1"),Bytes.toBytes("name"),Bytes.toBytes("marry"));
                put.addColumn(Bytes.toBytes("f1"),Bytes.toBytes("age"),Bytes.toBytes(33));
                put.addColumn(Bytes.toBytes("f1"),Bytes.toBytes("sex"),Bytes.toBytes("woman"));
                table.put(put);
                return null;
            }
        });
    }

    //Scan整表
    @Test
    public void testScan() {
        //scan可以加条件，都能执行
        List<People> peoples = hbaseTemplate.find("ns12:t1", new Scan(), new RowMapper<People>() {
            @Override
            public People mapRow(Result result, int rowNum) throws Exception {
                byte[] idByte = result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
                byte[] nameByte = result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
                byte[] ageByte = result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("age"));
                byte[] sexByte = result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("sex"));
                return new People(Bytes.toInt(idByte), Bytes.toString(nameByte), Bytes.toInt(ageByte), Bytes.toString(sexByte));
            }
        });
        for(People people : peoples){
            System.out.println(people);
        }
    }

    //获取单条数据
    @Test
    public void testGet() {
        People people = hbaseTemplate.get("ns12:t1", "r00001", new RowMapper<People>() {
            @Override
            public People mapRow(Result result, int rowNum) throws Exception {
                byte[] idByte = result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
                byte[] nameByte = result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
                byte[] ageByte = result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("age"));
                byte[] sexByte = result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("sex"));
                return new People(Bytes.toInt(idByte), Bytes.toString(nameByte), Bytes.toInt(ageByte), Bytes.toString(sexByte));
            }
        });
        System.out.println(people);
    }

    @Test
    public void testDelete() {
        hbaseTemplate.delete("ns12:t1","r00002","f1");
        hbaseTemplate.delete("ns12:t1","r00001","f1","name");
    }

    @Test
    public void addTestData() {
        hbaseTemplate.put("ns12:t1","r00005","f1","name",Bytes.toBytes("aa"));
    }


}
