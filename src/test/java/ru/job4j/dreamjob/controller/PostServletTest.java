package ru.job4j.dreamjob.controller;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.Store;
import ru.job4j.dreamjob.store.db.PostPsqlStore;
import ru.job4j.dreamjob.store.memory.PostMemStore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PostPsqlStore.class)
public class PostServletTest {

    @Test
    public void whenCreatePost() throws IOException {
        Store<Post> store = PostMemStore.instOf();
        PowerMockito.mockStatic(PostPsqlStore.class);
        PowerMockito.when(PostPsqlStore.getStore()).thenReturn(store);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        PowerMockito.when(req.getParameter("id")).thenReturn("0");
        PowerMockito.when(req.getParameter("name")).thenReturn("n");
        PowerMockito.when(req.getParameter("description")).thenReturn("d");
        new PostServlet().doPost(req, resp);
        Post result = store.findAll().iterator().next();
        Assert.assertThat(result.getName(), Is.is("n"));
        Assert.assertThat(result.getDescription(), Is.is("d"));
    }

    @Test
    public void whenUpdatePost() throws IOException {
        Store<Post> store = PostMemStore.instOf();
        PowerMockito.mockStatic(PostPsqlStore.class);
        PowerMockito.when(PostPsqlStore.getStore()).thenReturn(store);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        Post post = new Post();
        store.saveOrUpdate(post);
        PowerMockito.when(req.getParameter("id")).thenReturn(String.valueOf(post.getId()));
        PowerMockito.when(req.getParameter("name")).thenReturn("n");
        PowerMockito.when(req.getParameter("description")).thenReturn("d");
        new PostServlet().doPost(req, resp);
        Post result = store.findAll().iterator().next();
        Assert.assertThat(result.getName(), Is.is("n"));
        Assert.assertThat(result.getDescription(), Is.is("d"));
    }

}