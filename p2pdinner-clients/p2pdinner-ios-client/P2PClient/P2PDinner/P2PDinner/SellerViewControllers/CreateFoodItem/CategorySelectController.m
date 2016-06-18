//
//  CategorySelectController.m
//  P2PDinner
//
//  Created by Selvam M on 3/27/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "CategorySelectController.h"

@interface CategorySelectController ()

@end

@implementation CategorySelectController
- (void)doneButtonAction:(id)sender{
    NSSortDescriptor* sortDescriptor = [NSSortDescriptor sortDescriptorWithKey:@"name" ascending:YES selector:@selector(localizedStandardCompare:)];
    [self.delegate selectedCategors:[selectedCategory sortedArrayUsingDescriptors:[NSArray arrayWithObject:sortDescriptor]]];
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)naviagtionBarUISetup{
    
    UIColor *navBarColor=[UIColor colorWithRed:237.0/255.0 green:134.0/255.0 blue:0.0/255.0 alpha:1];
    NSDictionary *navbarTitleTextAttributes = [NSDictionary dictionaryWithObjectsAndKeys:
                                               [UIColor whiteColor],UITextAttributeTextColor,
                                               [UIFont fontWithName:@"Plantin" size:24], NSFontAttributeName,[NSValue valueWithUIOffset:UIOffsetMake(-1, 0)],UITextAttributeTextShadowOffset, nil];
    [navigationBar  setTitleTextAttributes:navbarTitleTextAttributes];
    [navigationBar setBarTintColor:navBarColor];
}

- (void)doneButtonAction{
    //NSLog(@"%@",[selectedCategory description]);
}

- (void)setupNavigationBar{
    [self naviagtionBarUISetup];
    UIBarButtonItem *rightBarButton = [[UIBarButtonItem alloc]
                                       initWithTitle:@"Done"
                                       style:UIBarButtonItemStylePlain
                                       target:self
                                       action:@selector(doneButtonAction:)];
    [rightBarButton setTintColor:[UIColor whiteColor]];
    self.navigationItem.rightBarButtonItem = rightBarButton;
}
-(NSArray *)shortOrderCategoryResponse:(NSArray *)resoseCategoryArray{
    NSMutableArray *categoryArray=[NSMutableArray arrayWithArray:resoseCategoryArray];
    NSPredicate *bobPredicate = [NSPredicate predicateWithFormat:@"name CONTAINS[cd] 'Other'"];
    CategoryItems *OthersObject;
    NSArray *arrayOfOthers=[resoseCategoryArray filteredArrayUsingPredicate:bobPredicate];
    if ([arrayOfOthers count]>0) {
        OthersObject=[arrayOfOthers objectAtIndex:0];
        if (OthersObject) {
            [categoryArray removeObject:OthersObject];
        }
        categoryArray=[NSMutableArray arrayWithArray:resoseCategoryArray];
        if (OthersObject) {
            [categoryArray addObject:OthersObject];
        }
        
    }
    NSSortDescriptor* sortDescriptor = [NSSortDescriptor sortDescriptorWithKey:@"name" ascending:YES selector:@selector(localizedStandardCompare:)];
    
    resoseCategoryArray=[categoryArray sortedArrayUsingDescriptors:[NSArray arrayWithObject:sortDescriptor]];
    
    return categoryArray;
}

- (void)callCategoryService{
    
    CategoryServiceHandler *categoryServiceHandler=[[CategoryServiceHandler alloc]init];
    [categoryServiceHandler getCategoryListItem:^(NSError *error, NSArray *respoceCategoryList) {
        
        [categoryList removeAllObjects];
        [categoryList addObjectsFromArray:[self shortOrderCategoryResponse:respoceCategoryList]];
        [categoryTableView reloadData];
        
    }];
}

- (void)viewDidLoad {
    [self callCategoryService];
    [super viewDidLoad];
    [self setupNavigationBar];
    self.navigationItem.title=@"Select Category";
    CategoryItems *categoryItems=[[CategoryItems alloc]init];
    [categoryItems setName:@"Loading..."];
    categoryList=[NSMutableArray arrayWithArray:[NSArray arrayWithObject:categoryItems]];
    selectedCategory=[[NSMutableArray alloc]init];
    categoryTableView.tableFooterView = [UIView new];
    [self.view setBackgroundColor:[UIColor colorWithRed:236.0/255.0 green:132.0/255.0 blue:0.0/255.0 alpha:1]];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return [categoryList count];
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell= (UITableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"CetegoryCell"];
    CategoryItems *categoryItems=[categoryList objectAtIndex:indexPath.row];
    cell.textLabel.text=[categoryItems name];
    cell.selectionStyle =UITableViewCellSelectionStyleNone;
    return cell;
}

- (void)removeAllSeletedIndex{
    NSArray *paths = [categoryTableView indexPathsForVisibleRows];
    for (NSIndexPath *path in paths) {
        UITableViewCell *cel=[categoryTableView cellForRowAtIndexPath:path];
        cel.accessoryType=UITableViewCellAccessoryNone;
    }
}

- (BOOL)isLastRow:(NSIndexPath *)indexpath{
    if ([categoryList count]-1==indexpath.row) {
        return TRUE;
    }
    return FALSE;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    if (cell.accessoryType != UITableViewCellAccessoryCheckmark) {
        if ([self isLastRow:indexPath]||[selectedCategory containsObject:[categoryList lastObject]]) {
            [self removeAllSeletedIndex];
            [selectedCategory removeAllObjects];
        }
        [selectedCategory addObject:[categoryList objectAtIndex:indexPath.row]];
        cell.accessoryType = UITableViewCellAccessoryCheckmark;
    }
    else{
        cell.accessoryType = UITableViewCellAccessoryNone;
        [selectedCategory removeObject:[categoryList objectAtIndex:indexPath.row]];
    }
    [cell setSelected:TRUE animated:TRUE];
}



@end
